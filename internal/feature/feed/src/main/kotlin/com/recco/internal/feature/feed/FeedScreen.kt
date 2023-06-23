package com.recco.internal.feature.feed

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
import androidx.compose.animation.core.Spring.StiffnessMedium
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.feature.feed.FeedUI
import com.recco.internal.feature.feed.FeedUIPreviewProvider
import com.recco.internal.feature.feed.FeedUserInteract
import com.recco.internal.feature.feed.FeedViewModel
import com.recco.internal.feature.feed.asSectionTitle
import com.recco.internal.core.model.feed.FeedSection
import com.recco.internal.core.model.feed.FeedSectionAndRecommendations
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppAlertDialog
import com.recco.internal.core.ui.components.AppEmptyContent
import com.recco.internal.core.ui.components.AppRecommendationCard
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTintedImagePeopleDigital
import com.recco.internal.core.ui.components.AppTintedImagePottedPlant2
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.EmptyState
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.heightRecommendationCard
import com.recco.internal.core.ui.components.widthRecommendationCard
import com.recco.internal.core.ui.extensions.asResExplanation
import com.recco.internal.core.ui.extensions.asResTitle
import com.recco.internal.core.ui.pipelines.GlobalViewEvent
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val LOCK_PLACEHOLDER_ELEMENTS = 5

@Composable
fun FeedRoute(
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic, FeedSectionType) -> Unit,
    navigateToBookmarks: () -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    FeedScreen(
        uiState = uiState,
        onUserInteract = { viewModel.onUserInteract(it) },
        navigateToQuestionnaire = navigateToQuestionnaire,
        navigateToBookmarks = navigateToBookmarks,
        navigateToArticle = navigateToArticle
    )
}

@Composable
private fun FeedScreen(
    uiState: UiState<FeedUI>,
    onUserInteract: (FeedUserInteract) -> Unit,
    navigateToQuestionnaire: (Topic, FeedSectionType) -> Unit,
    navigateToArticle: (ContentId) -> Unit,
    navigateToBookmarks: () -> Unit,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues()
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { AppTopBar() },
        backgroundColor = AppTheme.colors.background,
        contentPadding = contentPadding
    ) { innerPadding ->
        AppScreenStateAware(
            contentPadding = innerPadding,
            scrollState = scrollState,
            enablePullToRefresh = true,
            uiState = uiState,
            isEmpty = uiState.data?.sections.orEmpty().isEmpty(),
            retry = { onUserInteract(FeedUserInteract.Retry) },
            refresh = { onUserInteract(FeedUserInteract.Refresh) },
            emptyContent = {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    AppEmptyContent(
                        emptyState = EmptyState(
                            titleRes = R.string.recco_no_content_available_title_default,
                            drawableRes = R.drawable.bg_people_1,
                        )
                    )
                }
            },
            loadingHeaderContent = {
                Spacer(Modifier.height(AppSpacing.dp_40))
                FeedHeader(navigateToBookmarks)
                Spacer(Modifier.height(AppSpacing.dp_40))
            }
        ) { data ->
            FeedContent(
                feedUI = data,
                navigateToArticle = navigateToArticle,
                navigateToBookmarks = navigateToBookmarks,
                navigateToQuestionnaire = { topic, feedSectionType ->
                    navigateToQuestionnaire(topic, feedSectionType)
                },
                onLockAnimationFinished = {
                    onUserInteract(FeedUserInteract.RefreshUnlockedFeedSection)
                },
            )
        }
    }
}

@Composable
private fun FeedContent(
    feedUI: FeedUI,
    navigateToQuestionnaire: (Topic, FeedSectionType) -> Unit,
    navigateToArticle: (ContentId) -> Unit,
    navigateToBookmarks: () -> Unit,
    onLockAnimationFinished: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Spacer(Modifier.height(AppSpacing.dp_40))
        FeedHeader(navigateToBookmarks)
        Spacer(Modifier.height(AppSpacing.dp_40))

        feedUI.sections.forEach { section ->
            FeedSection(
                section = section,
                feedSectionToUnlock = feedUI.feedSectionToUnlock,
                navigateToArticle = navigateToArticle,
                navigateToQuestionnaire = navigateToQuestionnaire,
                onLockAnimationFinished = onLockAnimationFinished,
            )
            Spacer(Modifier.height(AppSpacing.dp_40))
        }
    }
}

@Composable
private fun FeedHeader(
    navigateToBookmarks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = AppSpacing.dp_24, end = AppSpacing.dp_12)
            ) {
                Icon(
                    modifier = Modifier.clickable { navigateToBookmarks() },
                    painter = painterResource(id = R.drawable.ic_bookmark_filled),
                    tint = AppTheme.colors.accent,
                    contentDescription = null,
                )
                Spacer(Modifier.height(AppSpacing.dp_16))

                Text(
                    text = stringResource(R.string.recco_welcome_back),
                    style = AppTheme.typography.h1
                )
                Text(
                    text = stringResource(R.string.recco_lets_make_toda_better),
                    style = AppTheme.typography.body1
                )
            }

            AppTintedImagePottedPlant2()
        }
    }
}

@Composable
private fun FeedSection(
    section: FeedSectionAndRecommendations,
    feedSectionToUnlock: GlobalViewEvent.FeedSectionToUnlock?,
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic, FeedSectionType) -> Unit,
    onLockAnimationFinished: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val openDialog = remember { mutableStateOf(false) }
    val topicDialog: MutableState<Topic?> = remember { mutableStateOf(null) }
    val feedSection = section.feedSection

    topicDialog.value?.let { topic ->
        QuestionnaireStartDialog(
            openDialog = openDialog,
            topic = topic,
            onClick = { navigateToQuestionnaire(topic, feedSection.type) },
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = AppSpacing.dp_24),
            text = feedSection.type.asSectionTitle(),
            style = AppTheme.typography.h4
        )
        Spacer(Modifier.height(AppSpacing.dp_16))

        Crossfade(
            targetState = feedSection.state == FeedSectionState.LOCKED,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            )
        ) { isLocked ->
            if (isLocked) {
                LockedItems(
                    scrollState = scrollState,
                    feedSection = feedSection,
                    feedSectionToUnlock = feedSectionToUnlock,
                    openDialog = openDialog,
                    topicDialog = topicDialog,
                    onLockAnimationFinished = onLockAnimationFinished
                )
            } else {
                UnlockedItems(
                    scrollState = scrollState,
                    section = section,
                    navigateToArticle = navigateToArticle,
                    navigateToQuestionnaire = navigateToQuestionnaire
                )
            }
        }
    }

    SideEffect {
        coroutineScope.launch {
            if (feedSection.type == feedSectionToUnlock?.type) {
                scrollState.scrollToItem(0)
            }
        }
    }
}

@Composable
private fun LockedItems(
    scrollState: LazyListState,
    feedSection: FeedSection,
    feedSectionToUnlock: GlobalViewEvent.FeedSectionToUnlock?,
    openDialog: MutableState<Boolean>,
    topicDialog: MutableState<Topic?>,
    onLockAnimationFinished: () -> Unit,
) {
    LazyRow(
        state = scrollState,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
        contentPadding = PaddingValues(
            start = AppSpacing.dp_24,
            end = AppSpacing.dp_24
        )
    ) {
        items(LOCK_PLACEHOLDER_ELEMENTS) {
            LockedCard(
                onClick = {
                    feedSection.topic?.let { topic ->
                        openDialog.value = true
                        topicDialog.value = topic
                    }
                },
                shouldStartAnimation = { feedSection.type == feedSectionToUnlock?.type },
                onAnimationFinished = onLockAnimationFinished
            )
        }
    }
}

@Composable
private fun UnlockedItems(
    scrollState: LazyListState,
    section: FeedSectionAndRecommendations,
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic, FeedSectionType) -> Unit,
) {
    LazyRow(
        state = scrollState,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
        contentPadding = PaddingValues(
            start = AppSpacing.dp_24,
            end = AppSpacing.dp_24
        )
    ) {
        items(
            items = section.recommendations,
            key = { item -> item.id.itemId }
        ) { recommendation ->
            AppRecommendationCard(recommendation, navigateToArticle)
        }

        if (section.feedSection.state == FeedSectionState.PARTIALLY_UNLOCKED) {
            item {
                PartiallyUnlockedCard {
                    section.feedSection.topic?.let { topic ->
                        navigateToQuestionnaire(topic, section.feedSection.type)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionnaireStartDialog(
    openDialog: MutableState<Boolean>,
    topic: Topic,
    onClick: () -> Unit,
) {
    AppAlertDialog(
        openDialog = openDialog,
        header = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.accent20),
                contentAlignment = Alignment.Center
            ) {
                AppTintedImagePeopleDigital(
                    modifier = Modifier.size(237.dp)
                )
            }
        },
        titleRes = topic.asResTitle(),
        descriptionRes = topic.asResExplanation(),
        textButtonPrimaryRes = R.string.recco_start,
        onClickPrimary = onClick
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PartiallyUnlockedCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(heightRecommendationCard)
            .width(widthRecommendationCard),
        elevation = 0.dp,
        onClick = onClick,
        backgroundColor = AppTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.dp_12),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                tint = AppTheme.colors.onPrimary,
                contentDescription = null,
            )
            Spacer(Modifier.height(AppSpacing.dp_8))
            Text(
                text = stringResource(R.string.recco_review_this_area),
                style = AppTheme.typography.h3.copy(color = AppTheme.colors.onPrimary),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LockedCard(
    onClick: () -> Unit,
    shouldStartAnimation: () -> Boolean,
    onAnimationFinished: () -> Unit,
) {
    val cardRes = remember {
        mutableStateOf(
            listOf(
                R.drawable.bg_no_rec_1,
                R.drawable.bg_no_rec_2,
                R.drawable.bg_no_rec_3
            ).random()
        )
    }

    Card(
        modifier = Modifier
            .height(heightRecommendationCard)
            .width(widthRecommendationCard),
        elevation = 0.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(cardRes.value),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppLockIcon(
                    shouldStartAnimation = shouldStartAnimation,
                    onAnimationFinished = onAnimationFinished
                )
                Text(
                    text = stringResource(R.string.recco_unlock),
                    style = AppTheme.typography.h3
                )
            }
        }
    }
}

@Composable
private fun AppLockIcon(
    shouldStartAnimation: () -> Boolean,
    onAnimationFinished: () -> Unit,
) {
    val transformationRotateSpec = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing,
    )
    val transformationBounceSpecEnter = tween<Float>(
        durationMillis = 300,
        easing = { OvershootInterpolator().getInterpolation(it) },
    )
    val transformationBounceSpecExit = spring<Float>(
        dampingRatio = DampingRatioHighBouncy,
        stiffness = StiffnessMedium
    )
    val iconRes = remember { mutableStateOf(R.drawable.ic_lock) }
    val rotateStart = 0f
    val rotateEnd = -20f
    val rotate = remember { mutableStateOf(rotateStart) }
    val scaleStart = 1f
    val scaleEnd = 1.3f
    val scale = remember { mutableStateOf(1f) }

    if (shouldStartAnimation()) {
        LaunchedEffect(Unit) {
            coroutineScope {
                launch {
                    delay(500)
                    animate(
                        initialValue = rotateStart,
                        targetValue = rotateEnd,
                        animationSpec = transformationRotateSpec
                    ) { value: Float, _: Float ->
                        rotate.value = value
                    }
                    iconRes.value = R.drawable.ic_unlock
                    animate(
                        initialValue = scaleStart,
                        targetValue = scaleEnd,
                        animationSpec = transformationBounceSpecEnter
                    ) { value: Float, _: Float ->
                        scale.value = value
                    }
                    animate(
                        initialValue = scaleEnd,
                        targetValue = scaleStart,
                        animationSpec = transformationBounceSpecExit
                    ) { value: Float, _: Float ->
                        scale.value = value
                    }
                }
            }

            onAnimationFinished()
        }
    }

    Icon(
        modifier = Modifier
            .graphicsLayer {
                this.scaleX = scale.value
                this.scaleY = scale.value
                this.alpha = 1f
                this.rotationZ = rotate.value
            },
        painter = painterResource(iconRes.value),
        contentDescription = null
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF, heightDp = 1000)
@Composable
private fun Preview(
    @PreviewParameter(FeedUIPreviewProvider::class) uiState: UiState<FeedUI>
) {
    FeedScreen(
        uiState = uiState,
        onUserInteract = {},
        navigateToQuestionnaire = { _, _ -> },
        navigateToArticle = {},
        navigateToBookmarks = {}
    )
}