package com.recco.internal.feature.feed

sealed class FeedUserInteract {
    object Retry : FeedUserInteract()
    object Refresh : FeedUserInteract()
    object RefreshUnlockedFeedSection : FeedUserInteract()
}