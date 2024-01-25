@file:UnstableApi package com.recco.internal.core.media

import android.app.PendingIntent
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
import com.recco.internal.core.ui.notifications.MediaNotificationManager
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class AudioPlayerState(
    val isPlaying: Boolean,
    val currentPosition: Long,
    val play: () -> Unit,
    val pause: () -> Unit,
    val release: () -> Unit,
    val seekTo: (Long) -> Unit,
    val trackDuration: Long?,
) {
    val isReady: Boolean
        get() = trackDuration != null
}

@Composable
fun rememberAudioPlayerState(
    trackItem: TrackItem,
): AudioPlayerState {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val isInPreviewMode = LocalInspectionMode.current
    var currentPosition by remember { mutableLongStateOf(0L) }
    var trackDuration by remember { mutableLongStateOf(0L)  }
    var isPlaying by remember { mutableStateOf(false) }

    val exoPlayer = remember(trackItem) {
        if (isInPreviewMode) {
            null
        } else {
            ExoPlayer.Builder(context).build()
        }
    }

    val sessionActivityPendingIntent = remember(context) {
        PendingIntent.getActivity(
            context,
            0,
            context.packageManager.getLaunchIntentForPackage(context.packageName),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    val mediaSession = remember(exoPlayer, sessionActivityPendingIntent) {
        if (exoPlayer == null) null else MediaSession.Builder(context, exoPlayer)
            .setId(trackItem.id)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
    }

    val notificationManager = remember(mediaSession, sessionActivityPendingIntent) {
        if (mediaSession == null) {
            null
        } else {
            MediaNotificationManager(
                context, mediaSession.token, exoPlayer!!
            )
        }
    }

    val player = remember {
        AudioPlayer(
            context = context,
            onIsPlayingChange = {
                isPlaying = it
            },
            exoPlayer = exoPlayer,
            mediaSession = mediaSession,
            mediaNotificationManager = notificationManager,
            onPlayerReady = { duration ->
                trackDuration = duration
            },
            onPositionChange = { newPosition ->
                currentPosition = newPosition
            }
        )
    }

    DisposableEffect(key1 = player) {
        onDispose {
            player.release()
        }
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

    return AudioPlayerState(
        isPlaying = isPlaying,
        currentPosition = currentPosition,
        play = { player.play() },
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
