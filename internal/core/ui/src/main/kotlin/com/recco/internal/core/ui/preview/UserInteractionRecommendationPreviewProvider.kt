package com.recco.internal.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation

internal class UserInteractionRecommendationPreviewProvider :
    PreviewParameterProvider<UserInteractionRecommendation> {
    override val values
        get() = sequenceOf(

            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.LIKE,
                isBookmarked = true,
                isBookmarkLoading = true
            ),

            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.LIKE,
                isBookmarked = true,
                isLikeLoading = true
            ),

            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.LIKE,
                isBookmarked = true,
                isDislikeLoading = true
            ),

            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.LIKE,
                isBookmarked = true
            ),

            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.DISLIKE,
                isBookmarked = false
            ),

            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.NOT_RATED,
                isBookmarked = true
            )
        )
}
