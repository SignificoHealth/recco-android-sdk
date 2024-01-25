package com.recco.internal.feature.article.audio

import android.util.Log
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.recco.internal.feature.article.isPlayerReady

class PlayerListener : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when (playbackState) {
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                isPlayerReady = true
            }
            else -> {
                isPlayerReady = false
            }
        }
    }


    override fun onIsLoadingChanged(isLoading: Boolean) {
        super.onIsLoadingChanged(isLoading)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Log.e("exo-player","Error: ${error.message}")
    }
}
