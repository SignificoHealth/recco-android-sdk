package com.recco.internal.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Recommendation
import com.recco.internal.core.model.recommendation.Status

class RecommendationPreviewProvider : PreviewParameterProvider<Recommendation> {
    override val values get() = sequenceOf(ARTICLE, AUDIO, VIDEO)

    companion object {
        val ARTICLE = Recommendation(
            id = ContentIdPreviewProvider.data,
            rating = Rating.LIKE,
            status = Status.VIEWED,
            headline = "Breathing techniques",
            bookmarked = true,
            imageUrl = null,
            type = ContentType.ARTICLE,
        )

        val AUDIO = Recommendation(
            id = ContentIdPreviewProvider.data,
            rating = Rating.LIKE,
            status = Status.VIEWED,
            headline = "Catching up with your hobbies",
            bookmarked = true,
            imageUrl = null,
            type = ContentType.AUDIO,
        )

        val VIDEO = Recommendation(
            id = ContentIdPreviewProvider.data,
            rating = Rating.LIKE,
            status = Status.VIEWED,
            headline = "Solving your emotions",
            bookmarked = true,
            imageUrl = null,
            type = ContentType.VIDEO,
        )
    }
}
