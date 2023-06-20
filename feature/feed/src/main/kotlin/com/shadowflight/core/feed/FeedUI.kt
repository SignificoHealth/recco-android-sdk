package com.shadowflight.core.feed

import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.FeedSectionType

data class FeedUI(
    val feedSectionTypeToUnlock: FeedSectionType? = null,
    val sections: List<FeedSectionAndRecommendations> = emptyList(),
)