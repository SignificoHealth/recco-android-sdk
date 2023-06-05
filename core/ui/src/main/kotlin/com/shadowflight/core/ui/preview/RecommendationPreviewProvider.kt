package com.shadowflight.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Recommendation
import com.shadowflight.core.model.recommendation.Status

class RecommendationPreviewProvider : PreviewParameterProvider<Recommendation> {
    override val values get() = sequenceOf(data)

    companion object {
        val data
            get() = Recommendation(
                id = ContentPreviewProvider.data,
                rating = Rating.LIKE,
                status = Status.VIEWED,
                headline = "Some headline",
                lead = "Some lead",
                imageUrl = null
            )
    }
}