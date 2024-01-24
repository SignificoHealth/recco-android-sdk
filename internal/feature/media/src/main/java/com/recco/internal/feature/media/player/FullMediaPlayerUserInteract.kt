package com.recco.internal.feature.media.player

sealed class FullMediaPlayerUserInteract {
    object Retry : FullMediaPlayerUserInteract()
    object ToggleBookmarkState : FullMediaPlayerUserInteract()
}
