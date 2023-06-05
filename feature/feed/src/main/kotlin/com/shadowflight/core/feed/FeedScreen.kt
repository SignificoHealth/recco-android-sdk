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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.recommendation.ContentId
import com.shadowflight.core.model.recommendation.Recommendation
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.preview.SectionAndRecommendationPreviewProvider
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme
import com.shadowflight.core.ui.extensions.viewedOverlay

@Composable
fun FeedRoute(
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = FeedViewUIState(
            isLoading = true
        )
    )
    FeedScreen(
        feedSectionAndRecommendations = uiState.feedSectionAndRecommendations,
        navigateToArticle = navigateToArticle,
        navigateToQuestionnaire = navigateToQuestionnaire
    )
}

@Composable
fun FeedScreen(
    feedSectionAndRecommendations: List<FeedSectionAndRecommendations>,
    navigateToQuestionnaire: (Topic) -> Unit,
    navigateToArticle: (ContentId) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        AppTopBar()
        Spacer(Modifier.height(AppSpacing.dp_24))
        FeedHeader()
        Spacer(Modifier.height(AppSpacing.dp_40))

        feedSectionAndRecommendations.forEach { feedSectionAndRecommendations ->
            FeedSection(
                feedSectionAndRecommendations = feedSectionAndRecommendations,
                navigateToArticle = navigateToArticle,
                navigateToQuestionnaire = navigateToQuestionnaire
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
                Text(text = stringResource(R.string.welcome_back), style = AppTheme.typography.h1)
                Text(
                    text = stringResource(R.string.lets_make_toda_better),
                    style = AppTheme.typography.body1
                )
            }
            Image(painter = painterResource(R.drawable.bg_plant), contentDescription = null)
        }
    }
}

private const val LOCK_PLACEHOLDER_ELEMENTS = 5

@Composable
private fun FeedSection(
    feedSectionAndRecommendations: FeedSectionAndRecommendations,
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = AppSpacing.dp_24),
            text = feedSectionAndRecommendations.feedSection.type.asSectionTitle(),
            style = AppTheme.typography.h4
        )
        Spacer(Modifier.height(AppSpacing.dp_16))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
            contentPadding = PaddingValues(
                start = AppSpacing.dp_24,
                end = AppSpacing.dp_24
            )
        ) {
            if (feedSectionAndRecommendations.feedSection.locked) {
                items(LOCK_PLACEHOLDER_ELEMENTS) {
                    LockedCard(onClick = {
                        feedSectionAndRecommendations.feedSection.topic?.let {
                            navigateToQuestionnaire(it)
                        }
                    })
                }
            } else {
                items(
                    items = feedSectionAndRecommendations.recommendations,
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

@Preview(heightDp = 1100)
@Composable
private fun FeedScreenPreview(
    @PreviewParameter(SectionAndRecommendationPreviewProvider::class) data: List<FeedSectionAndRecommendations>
) {
    FeedScreen(
        feedSectionAndRecommendations = data,
        navigateToArticle = {},
        navigateToQuestionnaire = {})
}