package com.shadowflight.core.feed

import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.ui.TriggerState

data class FeedUI(
    val triggerResetScrollState: TriggerState = TriggerState(),
    val sections: List<FeedSectionAndRecommendations> = emptyList(),
)