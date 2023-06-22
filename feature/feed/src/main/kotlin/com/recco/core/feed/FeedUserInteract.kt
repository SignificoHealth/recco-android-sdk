package com.recco.core.feed

sealed class FeedUserInteract {
    object Retry : FeedUserInteract()
    object Refresh : FeedUserInteract()
    object RefreshUnlockedFeedSection : FeedUserInteract()
}