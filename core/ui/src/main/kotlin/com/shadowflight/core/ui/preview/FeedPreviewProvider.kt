package com.shadowflight.core.ui.preview

import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.FeedSectionState
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic

class FeedPreviewProvider {

    companion object {
        fun data(
            type: FeedSectionType,
            state: FeedSectionState = FeedSectionState.LOCKED,
            recommendationsSize: Int = 10
        ) = FeedSectionAndRecommendations(
            recommendations = List(recommendationsSize) { RecommendationPreviewProvider.data },
            feedSection = FeedSection(
                type = type,
                state = state,
                topic = Topic.PHYSICAL_ACTIVITY
            )
        )
    }
}