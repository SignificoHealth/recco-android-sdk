package com.shadowflight.core.feed

import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed

data class FeedUI(
    val resetScrollPosition: StateEvent = consumed,
    val sections: List<FeedSectionAndRecommendations> = emptyList(),
)