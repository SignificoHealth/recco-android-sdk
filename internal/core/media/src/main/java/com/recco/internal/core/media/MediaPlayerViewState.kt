@file:UnstableApi

package com.recco.internal.core.media

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.recco.internal.core.model.media.MediaType
import com.recco.internal.core.model.recommendation.TrackItem
import com.recco.internal.core.ui.notifications.MediaNotificationManager
import com.recco.internal.core.ui.notifications.rememberPendingIntent
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class MediaPlayerViewState(
    val isPlaying: Boolean,
    val playerView: PlayerView,
    val play: () -> Unit,
    val pause: () -> Unit,
)

@Composable
fun rememberMediaPlayerStateWithLifecycle(trackItem: TrackItem): MediaPlayerViewState {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var currentPosition by remember { mutableLongStateOf(0L) }
    val exoPlayer: ExoPlayer? = rememberExoPlayer(trackItem)
    val playerView = rememberPlayerView(exoPlayer, trackItem)
    val lifecycleObserver = rememberPlayerLifecycleObserver(playerView, exoPlayer, trackItem.mediaType)
    var isNotificationShown by remember { mutableStateOf(false) }
    var isPlayingState by remember { mutableStateOf(false) }
    val sessionActivityPendingIntent = rememberPendingIntent()

    val mediaSession = remember(exoPlayer, sessionActivityPendingIntent) {
        if (exoPlayer != null && trackItem.mediaType == MediaType.AUDIO) {
            MediaSession.Builder(context, exoPlayer)
                .setId(trackItem.id)
                .setSessionActivity(sessionActivityPendingIntent)
                .build()
        } else {
            null
        }
    }

    val notificationManager = remember(mediaSession, sessionActivityPendingIntent) {
        if (mediaSession != null && trackItem.mediaType == MediaType.AUDIO) {
            MediaNotificationManager(context, mediaSession.token)
        } else {
            null
        }
    }

    LaunchedEffect(isPlayingState) {
        currentPosition = exoPlayer?.currentPosition?.coerceAtLeast(0) ?: 0

        while (isPlayingState) {
            currentPosition = exoPlayer?.currentPosition?.coerceAtLeast(0) ?: 0
            delay(1.seconds)
        }
    }

    LaunchedEffect(key1 = exoPlayer) {
        exoPlayer?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                isPlayingState = isPlaying

                if (!isNotificationShown) {
                    notificationManager?.showNotificationForPlayer(exoPlayer)
                    isNotificationShown = true
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    notificationManager?.hideNotification()
                    isNotificationShown = false
                }
            }
        })
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object: DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                notificationManager?.hideNotification()
                mediaSession?.release()
                super.onDestroy(owner)
            }
        }

        lifecycleOwner.lifecycle.apply {
            addObserver(lifecycleObserver)
            addObserver(observer)
        }
        onDispose {
            exoPlayer?.release()
            notificationManager?.hideNotification()
            lifecycleOwner.lifecycle.apply {
                removeObserver(observer)
                removeObserver(lifecycleObserver)
            }
        }
    }

    return MediaPlayerViewState(
        isPlaying = isPlayingState,
        playerView = playerView,
        play = { exoPlayer?.play() },
        pause = { exoPlayer?.pause() }
    )
}

@Composable
private fun rememberExoPlayer(
    trackItem: TrackItem
): ExoPlayer? {
    val context = LocalContext.current
    val isInPreviewMode = LocalInspectionMode.current

    return remember {
        if (!isInPreviewMode) {
            val player = ExoPlayer.Builder(context).build()
            player.setMediaItem(trackItem.asMediaItem())
            player.prepare()
            player
        } else {
            null
        }
    }
}

@Composable
private fun rememberPlayerView(
    exoPlayer: ExoPlayer?,
    trackItem: TrackItem
): PlayerView {
    val context = LocalContext.current

    return remember(trackItem) {
        val playerView = PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = false
            defaultArtwork = null
            artworkDisplayMode = PlayerView.ARTWORK_DISPLAY_MODE_FILL
        }

        loadArtworkWithCoil(context, trackItem, playerView)
        playerView
    }
}

@Composable
private fun rememberPlayerLifecycleObserver(
    player: PlayerView,
    exoPlayer: ExoPlayer?,
    mediaType: MediaType,
): LifecycleEventObserver {
    return remember(player) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    player.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    player.onPause()

                    // We want to pause the exoPlayer only if video on background
                    exoPlayer
                        ?.takeIf { mediaType == MediaType.VIDEO }
                        ?.pause()
                }
                Lifecycle.Event.ON_DESTROY -> player.player?.release()
                else -> {
                    // Do Nothing
                }
            }
        }
    }
}

private fun loadArtworkWithCoil(context: Context, trackItem: TrackItem, playerView: PlayerView) {
    val request = ImageRequest.Builder(context)
        .data(trackItem.imageUrl)
        .target { drawable ->
            val overlay = ColorDrawable(Color.BLACK).apply { alpha = (0.6 * 255).toInt() } // 0.6 alpha black overlay
            val combinedDrawable = LayerDrawable(arrayOf(drawable, overlay))
            playerView.defaultArtwork = combinedDrawable
        }
        .build()

    ImageLoader(context).enqueue(request)
}

