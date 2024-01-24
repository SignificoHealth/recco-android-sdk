package com.recco.internal.feature.video

sealed class FullMediaPlayerUserInteract {
    object Retry : FullMediaPlayerUserInteract()
    object ToggleBookmarkState : FullMediaPlayerUserInteract()
}
