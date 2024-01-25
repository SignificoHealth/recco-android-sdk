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
import androidx.media3.session.MediaSession
import com.recco.internal.core.model.recommendation.TrackItem
import com.recco.internal.core.ui.notifications.MediaNotificationManager

class AudioPlayer(
    private val context: Context,
    val exoPlayer: ExoPlayer?,
    val onIsPlayingChange: ((Boolean) -> Unit)? = null,
    val onPlayerReady: ((duration: Long) -> Unit)? = null,
    val onPositionChange: ((newPosition: Long) -> Unit)? = null,
    val mediaNotificationManager: MediaNotificationManager? = null,
    val mediaSession: MediaSession? = null
) {
    val currentPositionMs: Long
        get() = exoPlayer?.currentPosition?.coerceAtLeast(0) ?: 0

    private var isMediaNotificationShown = false

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

                    if (playbackState == Player.STATE_ENDED) {
                        isMediaNotificationShown = false
                        mediaNotificationManager?.hideNotification()
                    }

                    if (playbackState == Player.STATE_READY) {
                        onPlayerReady?.invoke(duration)
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    onIsPlayingChange?.invoke(isPlaying)
                }

                override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    if (reason == Player.DISCONTINUITY_REASON_SEEK || reason == Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT) {
                        onPositionChange?.invoke(newPosition.positionMs)
                    }
                }
            })
        }
    }


    fun play() {
        if (::trackItem.isInitialized) {
            if (exoPlayer != null) {
                exoPlayer.play()

                if (!isMediaNotificationShown) {
                    mediaNotificationManager?.showNotificationForPlayer(exoPlayer)
                    isMediaNotificationShown = true
                }
            }


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
        mediaNotificationManager?.hideNotification()
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
