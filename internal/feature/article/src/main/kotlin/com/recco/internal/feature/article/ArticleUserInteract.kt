package com.recco.internal.feature.article

internal sealed class ArticleUserInteract {
    object Retry : ArticleUserInteract()
    object ToggleBookmarkState : ArticleUserInteract()
    object ToggleLikeState : ArticleUserInteract()
    object ToggleDislikeState : ArticleUserInteract()
}