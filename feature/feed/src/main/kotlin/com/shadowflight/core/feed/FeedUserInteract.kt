package com.shadowflight.core.feed

import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic

sealed class FeedUserInteract {
    object Retry : FeedUserInteract()
    object Refresh : FeedUserInteract()
    object RefreshUnlockedFeedSection : FeedUserInteract()
    object ResetUnlockedFeedSection : FeedUserInteract()
}