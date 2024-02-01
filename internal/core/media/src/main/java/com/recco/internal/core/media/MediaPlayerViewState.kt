@file:UnstableApi

package com.recco.internal.core.media

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.recco.internal.core.model.media.MediaType
import com.recco.internal.core.model.recommendation.TrackItem
import com.recco.internal.core.ui.extensions.hasPermission
import com.recco.internal.core.ui.notifications.MediaNotificationManager
import com.recco.internal.core.ui.notifications.askForNotificationPermission
import com.recco.internal.core.ui.notifications.rememberPendingIntent
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class MediaPlayerViewState(
    val areControlsShown: Boolean,
    val isPlaying: Boolean,
    val playerView: PlayerView?,
    val play: () -> Unit,
    val pause: () -> Unit
)

@Composable
fun rememberMediaPlayerStateWithLifecycle(trackItem: TrackItem): MediaPlayerViewState {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var currentPosition by remember { mutableLongStateOf(0L) }
    val exoPlayer: ExoPlayer? = rememberExoPlayer(trackItem)
    val playerView = rememberPlayerView(exoPlayer, trackItem)
    val lifecycleObserver = rememberPlayerLifecycleObserver(playerView, exoPlayer, trackItem.mediaType)
    var isPlayingState by remember { mutableStateOf(false) }
    val sessionActivityPendingIntent = rememberPendingIntent()
    var isNotificationsPermissionGranted by remember { mutableStateOf(false) }
    var areControlsShown by remember { mutableStateOf(false) }

    val mediaSession = rememberMediaSession(
        exoPlayer = exoPlayer,
        sessionActivityPendingIntent = sessionActivityPendingIntent,
        trackItem = trackItem
    )

    val notificationManager = rememberMediaNotificationManager(
        mediaSession = mediaSession,
        sessionActivityPendingIntent = sessionActivityPendingIntent,
        trackItem = trackItem
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isNotificationsPermissionGranted = isGranted
        exoPlayer?.play()
        if (isGranted && exoPlayer != null) {
            notificationManager?.showNotificationForPlayer(exoPlayer)
        }
    }

    LaunchedEffect(key1 = playerView) {
        playerView?.setControllerVisibilityListener(
            PlayerView.ControllerVisibilityListener { visibility ->
                areControlsShown = visibility == View.VISIBLE
            }
        )
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
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    notificationManager?.hideNotification()
                }
            }
        })
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
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
            mediaSession?.release()
            notificationManager?.hideNotification()
            lifecycleOwner.lifecycle.apply {
                removeObserver(observer)
                removeObserver(lifecycleObserver)
            }
        }
    }

    return MediaPlayerViewState(
        isPlaying = isPlayingState,
        areControlsShown = areControlsShown,
        playerView = playerView ?: PlayerView(context).apply { visibility = View.GONE }, // Dummy view for preview
        play = {
            if (!isPlayingState) {
                if (context.hasPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                    exoPlayer?.let {
                        notificationManager?.showNotificationForPlayer(exoPlayer)
                        exoPlayer.play()
                    }
                } else if (!isNotificationsPermissionGranted && trackItem.mediaType == MediaType.AUDIO) {
                    permissionLauncher.askForNotificationPermission()
                } else {
                    exoPlayer?.play()
                }
            }
        },
        pause = { exoPlayer?.pause() }
    )
}

@Composable
private fun rememberMediaNotificationManager(
    mediaSession: MediaSession?,
    sessionActivityPendingIntent: PendingIntent?,
    trackItem: TrackItem
): MediaNotificationManager? {
    val context = LocalContext.current

    return remember(mediaSession, sessionActivityPendingIntent) {
        if (mediaSession != null && trackItem.mediaType == MediaType.AUDIO) {
            MediaNotificationManager(context, mediaSession.token)
        } else {
            null
        }
    }
}

private fun ExoPlayer.prepareFor(
    context: Context,
    trackItem: TrackItem
) {
    when (trackItem.mediaType) {
        MediaType.AUDIO -> {
            setMediaItem(trackItem.asMediaItem())
        }
        MediaType.VIDEO -> {
            val factory = DefaultDataSource.Factory(context)
            val source = HlsMediaSource.Factory(factory).createMediaSource(trackItem.asMediaItem())
            setMediaSource(source)
        }
    }

    prepare()
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
            player.prepareFor(context, trackItem)
            player
        } else {
            null
        }
    }
}

@Composable
private fun rememberMediaSession(
    exoPlayer: ExoPlayer?,
    sessionActivityPendingIntent: PendingIntent?,
    trackItem: TrackItem
): MediaSession? {
    val context = LocalContext.current

    return remember(exoPlayer, sessionActivityPendingIntent) {
        if (trackItem.mediaType == MediaType.AUDIO) {
            exoPlayer?.let { player ->
                sessionActivityPendingIntent?.let { pendingIntent ->
                    MediaSession.Builder(context, player)
                        .setId(trackItem.id)
                        .setSessionActivity(pendingIntent)
                        .build()
                }
            }
        } else {
            null
        }
    }
}

@Composable
private fun rememberPlayerView(
    exoPlayer: ExoPlayer?,
    trackItem: TrackItem
): PlayerView? {
    val context = LocalContext.current
    val isInPreviewMode = LocalInspectionMode.current

    return if (!isInPreviewMode) {
        remember(trackItem) {
            val playerView = PlayerView(context).apply {
                player = exoPlayer
                controllerAutoShow = false
                defaultArtwork = null
                setShowSubtitleButton(trackItem.mediaType == MediaType.VIDEO)
                artworkDisplayMode = PlayerView.ARTWORK_DISPLAY_MODE_FILL
            }

            loadArtworkWithCoil(context, trackItem, playerView)
            playerView
        }
    } else {
        null
    }
}

@Composable
private fun rememberPlayerLifecycleObserver(
    player: PlayerView?,
    exoPlayer: ExoPlayer?,
    mediaType: MediaType
): LifecycleEventObserver {
    return remember(player) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    player?.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    player?.onPause()

                    // We want to pause video on background, but not audio
                    exoPlayer
                        ?.takeIf { mediaType == MediaType.VIDEO }
                        ?.pause()
                }
                Lifecycle.Event.ON_DESTROY -> player?.player?.release()
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
