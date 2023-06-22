package com.recco.core.feed

import com.recco.core.model.feed.FeedSectionAndRecommendations
import com.recco.core.ui.pipelines.GlobalViewEvent

data class FeedUI(
    val feedSectionToUnlock: GlobalViewEvent.FeedSectionToUnlock? = null,
    val sections: List<FeedSectionAndRecommendations> = emptyList(),
)