package com.shadowflight.core.ui.models.article

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.components.AppProgressLoading
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme

data class UserInteractionRecommendation(
    val rating: Rating,
    val isBookmarked: Boolean = false,
    val isBookmarkLoading: Boolean = false,
    val isLikeLoading: Boolean = false,
    val isDislikeLoading: Boolean = false
)

@Composable
fun UserInteractionRecommendationCard(
    modifier: Modifier = Modifier,
    isScrollEndReached: Boolean = false,
    userInteraction: UserInteractionRecommendation,
    toggleBookmarkState: () -> Unit,
    toggleLikeState: () -> Unit,
    toggleDislikeState: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppSpacing.dp_24),
        elevation = animateDpAsState(targetValue = if (isScrollEndReached) 0.dp else 2.dp).value,
        backgroundColor = AppTheme.colors.background
    ) {
        val alphaDisabledColor = .5f

        Row(
            modifier = Modifier
                .padding(
                    horizontal = AppSpacing.dp_16,
                    vertical = AppSpacing.dp_12
                )
                .height(IntrinsicSize.Min)
        ) {
            Box(modifier = Modifier.size(AppSpacing.dp_24)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = userInteraction.isBookmarkLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AppProgressLoading(color = AppTheme.colors.accent, size = AppSpacing.dp_24)
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = !userInteraction.isBookmarkLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                            .clickable { toggleBookmarkState() },
                        painter = painterResource(
                            if (userInteraction.isBookmarked) {
                                R.drawable.ic_bookmark_filled
                            } else {
                                R.drawable.ic_bookmark_outline
                            }
                        ),
                        contentDescription = null,
                        tint = AppTheme.colors.accent,
                    )
                }
            }
            Spacer(Modifier.width(AppSpacing.dp_16))

            Divider(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(1.dp)
                    .fillMaxHeight()
            )
            Spacer(Modifier.width(AppSpacing.dp_16))

            Box(modifier = Modifier.size(AppSpacing.dp_24)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = userInteraction.isLikeLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AppProgressLoading(color = AppTheme.colors.accent, size = AppSpacing.dp_24)
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = !userInteraction.isLikeLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                            .clickable(
                                enabled = !userInteraction.isDislikeLoading,
                                onClick = toggleLikeState
                            ),
                        painter = painterResource(
                            if (userInteraction.rating == Rating.LIKE) {
                                R.drawable.ic_thumbs_up_filled
                            } else {
                                R.drawable.ic_thumbs_up_outline
                            }
                        ),
                        contentDescription = null,
                        tint = AppTheme.colors.accent.copy(
                            alpha = if (userInteraction.isDislikeLoading) alphaDisabledColor else 1f
                        ),
                    )
                }
            }
            Spacer(Modifier.width(AppSpacing.dp_16))

            Box(modifier = Modifier.size(AppSpacing.dp_24)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = userInteraction.isDislikeLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AppProgressLoading(color = AppTheme.colors.accent, size = AppSpacing.dp_24)
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = !userInteraction.isDislikeLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                            .clickable(
                                enabled = !userInteraction.isLikeLoading,
                                onClick = toggleDislikeState
                            ),
                        painter = painterResource(
                            if (userInteraction.rating == Rating.DISLIKE) {
                                R.drawable.ic_thumbs_down_filled
                            } else {
                                R.drawable.ic_thumbs_down_outline
                            }
                        ),
                        contentDescription = null,
                        tint = AppTheme.colors.accent.copy(
                            alpha = if (userInteraction.isLikeLoading) alphaDisabledColor else 1f
                        ),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun BookmarkLoadingPreview() {
    UserInteractionRecommendationCard(
        userInteraction = UserInteractionRecommendation(
            rating = Rating.LIKE,
            isBookmarked = true,
            isBookmarkLoading = true,
        ),
        toggleBookmarkState = {},
        toggleLikeState = {},
        toggleDislikeState = {}
    )
}

@Composable
@Preview
private fun LikeLoadingPreview() {
    UserInteractionRecommendationCard(
        userInteraction = UserInteractionRecommendation(
            rating = Rating.LIKE,
            isBookmarked = true,
            isLikeLoading = true
        ),
        toggleBookmarkState = {},
        toggleLikeState = {},
        toggleDislikeState = {}
    )
}

@Composable
@Preview
private fun DislikeLoadingPreview() {
    UserInteractionRecommendationCard(
        userInteraction = UserInteractionRecommendation(
            rating = Rating.LIKE,
            isBookmarked = true,
            isDislikeLoading = true
        ),
        toggleBookmarkState = {},
        toggleLikeState = {},
        toggleDislikeState = {}
    )
}

@Composable
@Preview
private fun LikedAndBookmarkedPreview() {
    UserInteractionRecommendationCard(
        userInteraction = UserInteractionRecommendation(
            rating = Rating.LIKE,
            isBookmarked = true
        ),
        toggleBookmarkState = {},
        toggleLikeState = {},
        toggleDislikeState = {}
    )
}

@Composable
@Preview
private fun DislikedAndNoBookmarkedPreview() {
    UserInteractionRecommendationCard(
        userInteraction = UserInteractionRecommendation(
            rating = Rating.DISLIKE,
            isBookmarked = false
        ),
        toggleBookmarkState = {},
        toggleLikeState = {},
        toggleDislikeState = {}
    )
}

@Composable
@Preview
private fun NoRatedPreview() {
    UserInteractionRecommendationCard(
        userInteraction = UserInteractionRecommendation(
            rating = Rating.NOT_RATED,
            isBookmarked = true
        ),
        toggleBookmarkState = {},
        toggleLikeState = {},
        toggleDislikeState = {}
    )
}