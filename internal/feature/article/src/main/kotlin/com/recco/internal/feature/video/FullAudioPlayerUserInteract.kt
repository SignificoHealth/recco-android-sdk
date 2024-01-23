package com.recco.internal.feature.video

sealed class FullAudioPlayerUserInteract {
    object Retry : FullAudioPlayerUserInteract()
    object ToggleBookmarkState : FullAudioPlayerUserInteract()
}
