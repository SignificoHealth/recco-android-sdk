@file:UnstableApi package com.recco.internal.core.media

import android.Manifest
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
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.recco.internal.core.model.recommendation.TrackItem
import com.recco.internal.core.ui.extensions.hasPermission
import com.recco.internal.core.ui.notifications.MediaNotificationManager
import com.recco.internal.core.ui.notifications.askForNotificationPermission
import com.recco.internal.core.ui.notifications.rememberPendingIntent
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberAudioPlayerState(
    trackItem: TrackItem,
): MediaPlayerState {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val isInPreviewMode = LocalInspectionMode.current
    var currentPosition by remember { mutableLongStateOf(0L) }
    var trackDuration by remember { mutableLongStateOf(0L)  }
    var isPlaying by remember { mutableStateOf(false) }
    var isNotificationsPermissionGranted by remember { mutableStateOf(false) }

    val exoPlayer = remember(trackItem) {
        if (!isInPreviewMode) {
            ExoPlayer.Builder(context).build()
        } else {
            null
        }
    }

    val pendingIntent = rememberPendingIntent()
    val mediaSession = remember(exoPlayer, pendingIntent) {
        if (exoPlayer != null) {
            pendingIntent?.let {
                MediaSession.Builder(context, exoPlayer)
                    .setId(trackItem.id)
                    .setSessionActivity(pendingIntent)
                    .build()
            }
        } else null
    }

    val notificationManager = remember(mediaSession, pendingIntent) {
        if (mediaSession != null) {
            MediaNotificationManager(context, mediaSession.token)
        } else {
            null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isNotificationsPermissionGranted = isGranted
        exoPlayer?.play()
        if (isGranted && exoPlayer != null) {
            notificationManager?.showNotificationForPlayer(exoPlayer)
        }
    }

    val player = remember {
        AudioPlayer(
            context = context,
            exoPlayer = exoPlayer,
            onIsPlayingChange = { isPlaying = it },
            onPlayerReady = { trackDuration = it },
            onPositionChange = { currentPosition = it },
            mediaNotificationManager = notificationManager
        )
    }

    DisposableEffect(key1 = player) {
        onDispose { player.release() }
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        val observer = object: DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                notificationManager?.hideNotification()
                player.release()
                super.onDestroy(owner)
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            notificationManager?.hideNotification()
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(isPlaying) {
        currentPosition = player.currentPositionMs

        while(isPlaying) {
            currentPosition = player.currentPositionMs
            delay(1.seconds)
        }
    }

    LaunchedEffect(trackItem) {
        player.load(trackItem)
    }

    return MediaPlayerState(
        isPlaying = isPlaying,
        currentPosition = currentPosition,
        play = {
            if (context.hasPermission(Manifest.permission.POST_NOTIFICATIONS)                ) {
                exoPlayer?.let {
                    notificationManager?.showNotificationForPlayer(exoPlayer)
                }

                player.play()

            } else if (!isNotificationsPermissionGranted) {
                permissionLauncher.askForNotificationPermission()

            } else {
                player.play()

            }
        },
        pause = { player.pause() },
        seekTo = {
            currentPosition = it
            player.seekTo(currentPosition)
        },
        release = {
            mediaSession?.release()
            player.release()
        },
        trackDuration = trackDuration
    )
}
