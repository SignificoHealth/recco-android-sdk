@file:OptIn(ExperimentalMaterialApi::class)

package com.shadowflight.core.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.insets.ui.Scaffold
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.recommendation.ContentId
import com.shadowflight.core.model.recommendation.Recommendation
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.components.AppEmptyContent
import com.shadowflight.core.ui.components.AppScreenStateAware
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.components.EmptyState
import com.shadowflight.core.ui.components.UiState
import com.shadowflight.core.ui.extensions.viewedOverlay
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.launch

@Composable
fun FeedRoute(
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic) -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    FeedScreen(
        uiState = uiState,
        onUserInteract = { viewModel.onUserInteract(it) },
        navigateToQuestionnaire = navigateToQuestionnaire,
        navigateToArticle = navigateToArticle
    )
}

@Composable
private fun FeedScreen(
    uiState: UiState<FeedUI>,
    onUserInteract: (FeedUserInteract) -> Unit,
    navigateToQuestionnaire: (Topic) -> Unit,
    navigateToArticle: (ContentId) -> Unit,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues()
) {
    val scrollState = rememberScrollState()
    val triggerResetScroll = remember { mutableStateOf(false) }

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
                            titleRes = R.string.no_content_available_title_default,
                            drawableRes = R.drawable.bg_people_1,
                        )
                    )
                }
            }
        ) { data ->
            EventEffect(
                event = data.resetScrollPosition,
                onConsumed = {
                    triggerResetScroll.value = true
                    onUserInteract(FeedUserInteract.ScrollConsumed)
                }
            ) { scrollState.scrollTo(0) }

            FeedContent(
                feedUI = data,
                navigateToArticle = navigateToArticle,
                navigateToQuestionnaire = navigateToQuestionnaire,
                triggerResetScroll = triggerResetScroll
            )
        }
    }
}

@Composable
private fun FeedContent(
    feedUI: FeedUI,
    navigateToQuestionnaire: (Topic) -> Unit,
    navigateToArticle: (ContentId) -> Unit,
    triggerResetScroll: MutableState<Boolean>,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Spacer(Modifier.height(AppSpacing.dp_40))
        FeedHeader()
        Spacer(Modifier.height(AppSpacing.dp_40))

        feedUI.sections.forEachIndexed { index, section ->
            FeedSection(
                section = section,
                navigateToArticle = navigateToArticle,
                navigateToQuestionnaire = navigateToQuestionnaire,
                resetScrollPosition = { scrollState ->
                    SideEffect {
                        coroutineScope.launch {
                            if (triggerResetScroll.value) {
                                scrollState.scrollToItem(0)

                                if (index == feedUI.sections.size - 1) {
                                    triggerResetScroll.value = false
                                }
                            }
                        }
                    }
                }
            )
            Spacer(Modifier.height(AppSpacing.dp_40))
        }
    }
}

@Composable
private fun FeedHeader() {
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
                Text(
                    text = stringResource(R.string.welcome_back),
                    style = AppTheme.typography.h1
                )
                Text(
                    text = stringResource(R.string.lets_make_toda_better),
                    style = AppTheme.typography.body1
                )
            }
            Image(
                painter = painterResource(R.drawable.bg_plant),
                contentDescription = null
            )
        }
    }
}

private const val LOCK_PLACEHOLDER_ELEMENTS = 5

@Composable
private fun FeedSection(
    resetScrollPosition: @Composable (LazyListState) -> Unit,
    section: FeedSectionAndRecommendations,
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = AppSpacing.dp_24),
            text = section.feedSection.type.asSectionTitle(),
            style = AppTheme.typography.h4
        )
        Spacer(Modifier.height(AppSpacing.dp_16))

        val scrollState = rememberLazyListState()

        resetScrollPosition(scrollState)

        LazyRow(
            state = scrollState,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
            contentPadding = PaddingValues(
                start = AppSpacing.dp_24,
                end = AppSpacing.dp_24
            )
        ) {
            if (section.feedSection.locked) {
                items(LOCK_PLACEHOLDER_ELEMENTS) {
                    LockedCard(onClick = {
                        section.feedSection.topic?.let {
                            navigateToQuestionnaire(it)
                        }
                    })
                }
            } else {
                items(
                    items = section.recommendations,
                    key = { it.id.itemId }) { recommendation ->
                    Card(recommendation, navigateToArticle)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Card(recommendation: Recommendation, onClick: (ContentId) -> Unit) {
    Card(
        modifier = Modifier
            .height(257.dp)
            .width(145.dp),
        elevation = AppTheme.elevation.card,
        onClick = { onClick(recommendation.id) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = recommendation.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .run {
                        if (recommendation.status == Status.VIEWED) {
                            viewedOverlay(AppTheme.colors.background)
                        } else {
                            this
                        }
                    },
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.background)
                    .padding(AppSpacing.dp_12)
                    .align(Alignment.BottomCenter),
                text = recommendation.headline,
                style = AppTheme.typography.body3,
                minLines = 2
            )
        }
    }
}

@Composable
private fun LockedCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(257.dp)
            .width(145.dp),
        elevation = 0.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(
                    listOf(
                        R.drawable.bg_no_rec_1,
                        R.drawable.bg_no_rec_2,
                        R.drawable.bg_no_rec_3
                    ).random()
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(AppSpacing.dp_24),
                    painter = painterResource(R.drawable.ic_unlock),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.unlock),
                    style = AppTheme.typography.h3
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun Preview(
    @PreviewParameter(FeedUIPreviewProvider::class) uiState: UiState<FeedUI>
) {
    FeedScreen(
        uiState = uiState,
        onUserInteract = {},
        navigateToQuestionnaire = {},
        navigateToArticle = {})
}
