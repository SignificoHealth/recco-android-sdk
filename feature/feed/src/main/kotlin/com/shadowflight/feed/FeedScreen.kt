package com.shadowflight.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.shadowflight.model.feed.FeedSectionAndRecommendations
import com.shadowflight.model.recommendation.Recommendation
import com.shadowflight.model.recommendation.Status
import com.shadowflight.ui.R
import com.shadowflight.uicommons.preview.SectionAndRecommendationPreviewProvider
import com.shadowflight.uicommons.theme.AppSpacing
import com.shadowflight.uicommons.theme.AppTheme
import com.shadowflight.uicommons.viewedOverlay

@Composable
fun FeedRoute(
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = FeedViewUIState(
            isLoading = true
        )
    )
    FeedScreen(uiState.feedSectionAndRecommendations)
}

@Composable
fun FeedScreen(
    feedSectionAndRecommendations: List<FeedSectionAndRecommendations>
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Spacer(Modifier.padding(top = AppSpacing.dp_24))
        feedSectionAndRecommendations.forEach { feedSectionAndRecommendations ->
            FeedSection(feedSectionAndRecommendations)
            Spacer(Modifier.height(AppSpacing.dp_40))
        }
    }
}

@Composable
private fun FeedSection(feedSectionAndRecommendations: FeedSectionAndRecommendations) {
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
                items(5) { LockedCard() }
            } else {
                items(
                    items = feedSectionAndRecommendations.recommendations,
                    key = { it.id.itemId }) { recommendation ->
                    Card(recommendation)
                }
            }
        }
    }
}

@Composable
private fun Card(recommendation: Recommendation) {
    Card(
        modifier = Modifier
            .height(257.dp)
            .width(145.dp),
        elevation = AppTheme.elevation.card
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
private fun LockedCard() {
    Card(
        modifier = Modifier
            .height(257.dp)
            .width(145.dp),
        elevation = 0.dp
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
    FeedScreen(data)
}