package com.recco.internal.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Recommendation
import com.recco.internal.core.model.recommendation.Status

class RecommendationPreviewProvider : PreviewParameterProvider<Recommendation> {
    override val values get() = sequenceOf(data)

    companion object {
        val data
            get() = Recommendation(
                id = ContentIdPreviewProvider.data,
                rating = Rating.LIKE,
                status = Status.VIEWED,
                headline = "Some headline",
                lead = "Some lead",
                bookmarked = true,
                imageUrl = null
            )
    }
}