package com.shadowflight.core.ui.preview

import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic

class FeedPreviewProvider  {

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