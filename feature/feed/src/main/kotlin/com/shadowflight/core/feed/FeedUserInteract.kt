package com.shadowflight.core.feed

sealed class FeedUserInteract {
    object Retry : FeedUserInteract()
    object Refresh : FeedUserInteract()
    object ScrollConsumed : FeedUserInteract()
}