package com.shadowflight.core.feed

import com.shadowflight.core.model.feed.FeedSectionAndRecommendations

data class FeedUI(
    val resetScrollPosition: Boolean = false,
    val sections: List<FeedSectionAndRecommendations> = emptyList(),
)