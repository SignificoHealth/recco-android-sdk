package com.recco.internal.feature.article

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppProgressLoading
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.internal.feature.article.preview.UserInteractionRecommendationPreviewProvider

internal data class UserInteractionRecommendation(
    val rating: Rating,
    val isBookmarked: Boolean = false,
    val isBookmarkLoading: Boolean = false,
    val isLikeLoading: Boolean = false,
    val isDislikeLoading: Boolean = false
)

@Composable
internal fun UserInteractionRecommendationCard(
    modifier: Modifier = Modifier,
    isScrollEndReached: Boolean = false,
    userInteraction: UserInteractionRecommendation,
    toggleBookmarkState: () -> Unit,
    toggleLikeState: () -> Unit,
    toggleDislikeState: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppSpacing.dp_32),
        elevation = animateDpAsState(targetValue = if (isScrollEndReached) 0.dp else 2.dp).value,
        backgroundColor = AppTheme.colors.background
    ) {
        val alphaDisabledColor = .5f
        val bookmarkDescription = stringResource(
            if (userInteraction.isBookmarked) {
                R.string.accessibility_remove_from_bookmarks
            } else {
                R.string.accessibility_add_to_bookmarks
            }
        )
        val thumbsUpDescription = stringResource(
            if (userInteraction.rating == Rating.LIKE) {
                R.string.accessibility_remove_thumbs_up
            } else {
                R.string.accessibility_give_thumbs_up
            }
        )
        val thumbsDownDescription = stringResource(
            if (userInteraction.rating == Rating.DISLIKE) {
                R.string.accessibility_remove_thumbs_down
            } else {
                R.string.accessibility_give_thumbs_down
            }
        )
        Row(
            modifier = Modifier
                .padding(
                    horizontal = AppSpacing.dp_12,
                    vertical = AppSpacing.dp_8
                )
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .semantics(mergeDescendants = true) {}
                    .let {
                        if (userInteraction.isBookmarkLoading) {
                            it
                        } else {
                            it.clickable { toggleBookmarkState() }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
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
                            .semantics { contentDescription = bookmarkDescription },
                        painter = painterResource(
                            if (userInteraction.isBookmarked) {
                                R.drawable.recco_ic_bookmark_filled
                            } else {
                                R.drawable.recco_ic_bookmark_outline
                            }
                        ),
                        tint = if (userInteraction.isBookmarked) {
                            AppTheme.colors.accent
                        } else {
                            AppTheme.colors.primary
                        },
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.width(AppSpacing.dp_12))

            Divider(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(1.dp)
                    .fillMaxHeight()
            )
            Spacer(Modifier.width(AppSpacing.dp_12))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .semantics(mergeDescendants = true) {}
                    .let {
                        if (userInteraction.isLikeLoading) {
                            it
                        } else {
                            it.clickable(
                                enabled = !userInteraction.isDislikeLoading,
                                onClick = toggleLikeState
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
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
                            .semantics { contentDescription = thumbsUpDescription },
                        painter = painterResource(
                            if (userInteraction.rating == Rating.LIKE) {
                                R.drawable.recco_ic_thumbs_up_filled
                            } else {
                                R.drawable.recco_ic_thumbs_up_outline
                            }
                        ),
                        tint = if (userInteraction.rating == Rating.LIKE) {
                            AppTheme.colors.accent.copy(
                                alpha = if (userInteraction.isDislikeLoading) alphaDisabledColor else 1f
                            )
                        } else {
                            AppTheme.colors.primary.copy(
                                alpha = if (userInteraction.isDislikeLoading) alphaDisabledColor else 1f
                            )
                        },
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.width(AppSpacing.dp_8 / 2))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .semantics(mergeDescendants = true) {}
                    .let {
                        if (userInteraction.isDislikeLoading) {
                            it
                        } else {
                            it.clickable(
                                enabled = !userInteraction.isLikeLoading,
                                onClick = toggleDislikeState
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
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
                            .semantics { contentDescription = thumbsDownDescription },
                        painter = painterResource(
                            if (userInteraction.rating == Rating.DISLIKE) {
                                R.drawable.recco_ic_thumbs_down_filled
                            } else {
                                R.drawable.recco_ic_thumbs_down_outline
                            }
                        ),
                        tint = if (userInteraction.rating == Rating.DISLIKE) {
                            AppTheme.colors.accent.copy(
                                alpha = if (userInteraction.isDislikeLoading) alphaDisabledColor else 1f
                            )
                        } else {
                            AppTheme.colors.primary.copy(
                                alpha = if (userInteraction.isDislikeLoading) alphaDisabledColor else 1f
                            )
                        },
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview(
    @PreviewParameter(UserInteractionRecommendationPreviewProvider::class) userInteraction:
    UserInteractionRecommendation
) {
    AppTheme {
        UserInteractionRecommendationCard(
            userInteraction = userInteraction,
            toggleBookmarkState = {},
            toggleLikeState = {},
            toggleDislikeState = {}
        )
    }
}

@Composable
@Preview
private fun PreviewDark(
    @PreviewParameter(UserInteractionRecommendationPreviewProvider::class) userInteraction:
    UserInteractionRecommendation
) {
    AppTheme(darkTheme = true) {
        UserInteractionRecommendationCard(
            userInteraction = userInteraction,
            toggleBookmarkState = {},
            toggleLikeState = {},
            toggleDislikeState = {}
        )
    }
}
