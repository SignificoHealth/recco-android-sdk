package com.recco.internal.feature.bookmark

sealed class BookmarkUserInteract {
    object Retry : BookmarkUserInteract()
    object Refresh : BookmarkUserInteract()
}