package com.recco.internal.feature.feed

import com.recco.internal.core.model.feed.FeedSectionAndRecommendations
import com.recco.internal.core.ui.pipelines.GlobalViewEvent

internal data class FeedUI(
    val feedSectionToUnlock: GlobalViewEvent.FeedSectionToUnlock? = null,
    val sections: List<FeedSectionAndRecommendations> = emptyList()
)
