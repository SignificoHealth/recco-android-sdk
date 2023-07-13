package com.recco.internal.feature.article.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.feature.article.UserInteractionRecommendation

internal class UserInteractionRecommendationPreviewProvider :
    PreviewParameterProvider<UserInteractionRecommendation> {
    override val values
        get() = sequenceOf(

            UserInteractionRecommendation(
                rating = Rating.LIKE,
                isBookmarked = true,
                isBookmarkLoading = true,
            ),

            UserInteractionRecommendation(
                rating = Rating.LIKE,
                isBookmarked = true,
                isLikeLoading = true
            ),

            UserInteractionRecommendation(
                rating = Rating.LIKE,
                isBookmarked = true,
                isDislikeLoading = true
            ),

            UserInteractionRecommendation(
                rating = Rating.LIKE,
                isBookmarked = true
            ),

            UserInteractionRecommendation(
                rating = Rating.DISLIKE,
                isBookmarked = false
            ),

            UserInteractionRecommendation(
                rating = Rating.NOT_RATED,
                isBookmarked = true
            )
        )
}