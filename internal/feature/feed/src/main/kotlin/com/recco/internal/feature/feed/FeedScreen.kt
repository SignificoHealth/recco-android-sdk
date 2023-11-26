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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.core.model.FlowDataState
import com.recco.internal.core.model.feed.FeedSection
import com.recco.internal.core.model.feed.FeedSectionAndRecommendations
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.Recommendation
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppAlertDialog
import com.recco.internal.core.ui.components.AppQuestionnaireCard
import com.recco.internal.core.ui.components.AppRecommendationCard
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTintedImagePeopleDigital
import com.recco.internal.core.ui.components.AppTintedImagePottedPlant
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.heightRecommendationCard
import com.recco.internal.core.ui.components.widthRecommendationCard
import com.recco.internal.core.ui.extensions.asResExplanation
import com.recco.internal.core.ui.extensions.asResTitle
import com.recco.internal.core.ui.extensions.shimmerEffect
import com.recco.internal.core.ui.pipelines.GlobalViewEvent
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val LOCK_PLACEHOLDER_ELEMENTS = 5
private const val LOADING_PLACEHOLDER_ELEMENTS = 3

@Composable
internal fun FeedRoute(
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic, FeedSectionType, ContentId?) -> Unit,
    navigateToBookmarks: () -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
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
    navigateToQuestionnaire: (Topic, FeedSectionType, ContentId?) -> Unit,
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
                navigateToQuestionnaire = navigateToQuestionnaire,
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
    navigateToQuestionnaire: (Topic, FeedSectionType, ContentId?) -> Unit,
    navigateToArticle: (ContentId) -> Unit,
    navigateToBookmarks: () -> Unit,
    onLockAnimationFinished: () -> Unit
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
            Crossfade(
                label = "crossfade_feed_content",
                targetState = section.recommendations is FlowDataState.Loading,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            ) { isLoading ->
                Column {
                    if (isLoading) {
                        FeedSectionLoading(section = section)
                    } else {
                        FeedSection(
                            section = section,
                            feedSectionToUnlock = feedUI.feedSectionToUnlock,
                            navigateToArticle = navigateToArticle,
                            onLockAnimationFinished = onLockAnimationFinished,
                            navigateToQuestionnaire = navigateToQuestionnaire
                        )
                    }
                    Spacer(Modifier.height(AppSpacing.dp_40))
                }
            }
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
                    painter = painterResource(id = R.drawable.recco_ic_bookmark_filled),
                    tint = AppTheme.colors.accent,
                    contentDescription = null
                )
                Spacer(Modifier.height(AppSpacing.dp_16))

                Text(
                    text = stringResource(R.string.recco_dashboard_welcome_back_title),
                    style = AppTheme.typography.h1
                )
                Text(
                    text = stringResource(R.string.recco_dashboard_welcome_back_body),
                    style = AppTheme.typography.body1
                )
            }

            AppTintedImagePottedPlant()
        }
    }
}

@Composable
private fun FeedSectionLoading(
    section: FeedSectionAndRecommendations
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = AppSpacing.dp_24),
            text = section.feedSection.type.asSectionTitle(),
            style = AppTheme.typography.h4
        )
        Spacer(Modifier.height(AppSpacing.dp_16))
        LoadingItems()
    }
}

@Composable
private fun FeedSection(
    section: FeedSectionAndRecommendations,
    feedSectionToUnlock: GlobalViewEvent.FeedSectionToUnlock?,
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic, FeedSectionType, ContentId?) -> Unit,
    onLockAnimationFinished: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val openDialog = remember { mutableStateOf(false) }
    val topicDialog: MutableState<Topic?> = remember { mutableStateOf(null) }
    val contentIdDialog: MutableState<ContentId?> = remember { mutableStateOf(null) }
    val feedSection = section.feedSection

    topicDialog.value?.let { topic ->
        QuestionnaireStartDialog(
            openDialog = openDialog,
            topic = topic,
            onClick = { navigateToQuestionnaire(topic, feedSection.type, contentIdDialog.value) }
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
            label = "crossfade_feed_section",
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
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
                    openDialog = openDialog,
                    topicDialog = topicDialog,
                    contentIdDialog = contentIdDialog,
                    navigateToArticle = navigateToArticle,
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
private fun LoadingItems() {
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
        contentPadding = PaddingValues(
            start = AppSpacing.dp_24,
            end = AppSpacing.dp_24
        )
    ) {
        items(LOADING_PLACEHOLDER_ELEMENTS) {
            Card(
                modifier = Modifier
                    .height(heightRecommendationCard)
                    .width(widthRecommendationCard),
                elevation = AppTheme.elevation.card
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect(index = it)
                )
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
    onLockAnimationFinished: () -> Unit
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
    openDialog: MutableState<Boolean>,
    topicDialog: MutableState<Topic?>,
    contentIdDialog: MutableState<ContentId?>,
    navigateToArticle: (ContentId) -> Unit,
) {
    val recommendations =
        (section.recommendations as FlowDataState.Success<List<Recommendation>>).data
    LazyRow(
        state = scrollState,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
        contentPadding = PaddingValues(
            start = AppSpacing.dp_24,
            end = AppSpacing.dp_24
        )
    ) {
        items(
            items = recommendations,
            key = { item -> item.id.itemId }
        ) { recommendation ->
            when (recommendation.type) {
                ContentType.ARTICLE -> {
                    AppRecommendationCard(recommendation, navigateToArticle)
                }

                ContentType.QUESTIONNAIRE -> {
                    AppQuestionnaireCard(section.feedSection.topic!!) {
                        openDialog.value = true
                        topicDialog.value = section.feedSection.topic
                        contentIdDialog.value = recommendation.id
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
    onClick: () -> Unit
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
private fun LockedCard(
    onClick: () -> Unit,
    shouldStartAnimation: () -> Boolean,
    onAnimationFinished: () -> Unit
) {
    val cardRes = remember {
        mutableStateOf(
            listOf(
                R.drawable.recco_bg_no_rec_1,
                R.drawable.recco_bg_no_rec_2,
                R.drawable.recco_bg_no_rec_3
            ).random()
        )
    }

    Card(
        modifier = Modifier
            .height(heightRecommendationCard)
            .width(widthRecommendationCard),
        elevation = 0.dp,
        onClick = onClick,
        backgroundColor = AppTheme.colors.background
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
                    text = stringResource(R.string.recco_dashboard_unlock),
                    style = AppTheme.typography.h3
                )
            }
        }
    }
}

@Composable
private fun AppLockIcon(
    shouldStartAnimation: () -> Boolean,
    onAnimationFinished: () -> Unit
) {
    val transformationRotateSpec = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
    val transformationBounceSpecEnter = tween<Float>(
        durationMillis = 300,
        easing = { OvershootInterpolator().getInterpolation(it) }
    )
    val transformationBounceSpecExit = spring<Float>(
        dampingRatio = DampingRatioHighBouncy,
        stiffness = StiffnessMedium
    )
    val iconRes = remember { mutableStateOf(R.drawable.recco_ic_lock) }
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
                    iconRes.value = R.drawable.recco_ic_unlock
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
        contentDescription = null,
        tint = AppTheme.colors.primary
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF, heightDp = 1250)
@Composable
private fun Preview(
    @PreviewParameter(FeedUIPreviewProvider::class) uiState: UiState<FeedUI>
) {
    AppTheme {
        FeedScreen(
            uiState = uiState,
            onUserInteract = {},
            navigateToArticle = {},
            navigateToBookmarks = {},
            navigateToQuestionnaire = { _, _, _ -> }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF, heightDp = 1250)
@Composable
private fun PreviewDark(
    @PreviewParameter(FeedUIPreviewProvider::class) uiState: UiState<FeedUI>
) {
    AppTheme(
        darkTheme = true
    ) {
        FeedScreen(
            uiState = uiState,
            onUserInteract = {},
            navigateToArticle = {},
            navigateToBookmarks = {},
            navigateToQuestionnaire = { _, _, _ -> }
        )
    }
}
