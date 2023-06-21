package com.shadowflight.core.feed

import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.ui.pipelines.GlobalViewEvent

data class FeedUI(
    val feedSectionToUnlock: GlobalViewEvent.FeedSectionUnlock? = null,
    val sections: List<FeedSectionAndRecommendations> = emptyList(),
)