package com.shadowflight.core.feed

import androidx.compose.animation.core.FiniteAnimationSpec
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.ui.TriggerState

data class FeedUI(
    val feedSectionTypeToUnlock: FeedSectionType? = null,
    val resetScrollTriggerState: TriggerState = TriggerState(),
    val sections: List<FeedSectionAndRecommendations> = emptyList(),
)