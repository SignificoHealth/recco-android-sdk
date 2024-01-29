package com.recco.internal.feature.article.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation

internal class ContentUserInteractionPreviewProvider :
    PreviewParameterProvider<UserInteractionRecommendation?> {
    override val values
        get() = sequenceOf(
            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.LIKE
            ),
            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.DISLIKE
            ),
            UserInteractionRecommendation(
                contentId = ContentId(itemId = "1", catalogId = "1"),
                rating = Rating.DISLIKE,
                isBookmarked = true
            ),
            null
        )
}
