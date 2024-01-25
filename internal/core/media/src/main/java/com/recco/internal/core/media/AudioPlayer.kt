@file:OptIn(UnstableApi::class) package com.recco.internal.core.media

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.recco.internal.core.model.recommendation.TrackItem

class AudioPlayer(
    private val context: Context,
    val exoPlayer: ExoPlayer?,
    val onTrackEnded: (() -> Unit)? = null,
    val onIsPlayingChange: ((Boolean) -> Unit)? = null,
    val onPlayerReady: ((duration: Long) -> Unit)? = null
) {
    val currentPositionMs: Long
        get() = exoPlayer?.currentPosition?.coerceAtLeast(0) ?: 0

    private lateinit var trackItem: TrackItem

    init {
        setupPlayer()
    }

    private fun setupPlayer() {
        exoPlayer?.apply {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build()

            setAudioAttributes(audioAttributes, true)
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)

                    if (playbackState == Player.STATE_ENDED)
                        onTrackEnded?.invoke()

                    if (playbackState == Player.STATE_READY)
                        onPlayerReady?.invoke(duration)
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    onIsPlayingChange?.invoke(isPlaying)
                }
            })
        }
    }

    fun play() {
        if (::trackItem.isInitialized) {
            exoPlayer?.play()
        } else {
            throw UninitializedPropertyAccessException(
                "You must call load(TrackItem) before playing the player"
            )
        }

        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun release() {
        exoPlayer?.release()
    }

    fun load(trackItem: TrackItem) {
        this.trackItem = trackItem

        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(trackItem.asMediaItem())

        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
    }


    fun seekTo(currentPosition: Long) {
        exoPlayer?.seekTo(currentPosition)
    }
}
