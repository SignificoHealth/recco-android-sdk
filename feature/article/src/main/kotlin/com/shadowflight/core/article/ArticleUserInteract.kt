package com.shadowflight.core.article

sealed class ArticleUserInteract {
    object Retry : ArticleUserInteract()
    object ToggleBookmarkState : ArticleUserInteract()
    object ToggleLikeState : ArticleUserInteract()
    object ToggleDislikeState : ArticleUserInteract()
}