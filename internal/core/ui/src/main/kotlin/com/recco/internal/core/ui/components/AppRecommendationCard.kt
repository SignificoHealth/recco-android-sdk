package com.recco.internal.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Recommendation
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.ui.extensions.viewedOverlay
import com.recco.internal.core.ui.preview.RecommendationPreviewProvider
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

val heightRecommendationCard = 257.dp
val widthRecommendationCard = 145.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppRecommendationCard(
    recommendation: Recommendation,
    onClick: (ContentId) -> Unit,
    applyViewedOverlay: Boolean = true
) {
    Card(
        modifier = Modifier
            .height(heightRecommendationCard)
            .width(widthRecommendationCard),
        elevation = AppTheme.elevation.card,
        onClick = { onClick(recommendation.id) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AppAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .run {
                        if (recommendation.status == Status.VIEWED && applyViewedOverlay) {
                            viewedOverlay(AppTheme.colors.background)
                        } else {
                            this
                        }
                    },
                data = recommendation.imageUrl,
                alt = recommendation.imageAlt,
                contentScale = ContentScale.Crop,
                loadingAnimationDrawable = loadingCardAnimationDrawable()
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.background)
                    .padding(AppSpacing.dp_12)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = recommendation.headline,
                    style = AppTheme.typography.body3,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                RecommendationTypeRow(
                    iconSize = 16.dp,
                    iconSpacing = 4.dp,
                    textStyle = AppTheme.typography.labelSmall.copy(
                        color = AppTheme.colors.primary.copy(alpha = 0.6f),
                        fontSize = 10.sp
                    ),
                    contentType = recommendation.type,
                    lengthInSeconds = recommendation.lengthInSeconds,
                    modifier = Modifier.alpha(0.8f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(RecommendationPreviewProvider::class) recommendation: Recommendation
) {
    AppTheme {
        AppRecommendationCard(
            recommendation = recommendation,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(RecommendationPreviewProvider::class) recommendation: Recommendation
) {
    AppTheme(darkTheme = true) {
        AppRecommendationCard(
            recommendation = recommendation,
            onClick = {}
        )
    }
}
