package com.recco.internal.feature.bookmark

internal sealed class BookmarkUserInteract {
    object Retry : BookmarkUserInteract()
    object Refresh : BookmarkUserInteract()
}