package com.recco.internal.core.media

class MediaPlayerState(
    val isPlaying: Boolean,
    val currentPosition: Long,
    val play: () -> Unit,
    val pause: () -> Unit,
    val release: () -> Unit,
    val seekTo: (Long) -> Unit,
    val trackDuration: Long?
) {
    val isReady: Boolean
        get() = trackDuration != null
}
