package com.shadowflight.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.ui.models.UiState

class FeedUIPreviewProvider :
    PreviewParameterProvider<UiState<List<FeedSectionAndRecommendations>>> {

    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = listOf(
                    data(
                        type = FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS,
                        locked = false
                    ),
                    data(
                        type = FeedSectionType.NUTRITION_EXPLORE,
                        locked = false
                    ),
                    data(
                        type = FeedSectionType.SLEEP_RECOMMENDATIONS,
                        locked = true
                    )
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )

    companion object {
        fun data(type: FeedSectionType, locked: Boolean) = FeedSectionAndRecommendations(
            recommendations = List(10) { RecommendationPreviewProvider.data },
            feedSection = FeedSection(
                type = type,
                locked = locked,
                topic = Topic.PHYSICAL_ACTIVITY
            )
        )
    }
}