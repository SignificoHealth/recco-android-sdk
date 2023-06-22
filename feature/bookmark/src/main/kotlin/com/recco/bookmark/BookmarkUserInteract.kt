package com.recco.bookmark

sealed class BookmarkUserInteract {
    object Retry : BookmarkUserInteract()
    object Refresh : BookmarkUserInteract()
}