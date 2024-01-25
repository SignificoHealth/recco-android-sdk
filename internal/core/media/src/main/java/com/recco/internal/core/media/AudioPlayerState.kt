@file:UnstableApi package com.recco.internal.core.media

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
import com.recco.internal.core.model.recommendation.TrackItem
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class AudioPlayerState(
    val audioPlayer: AudioPlayer?,
    val isPlaying: Boolean,
    val currentPosition: Long,
    val play: () -> Unit,
    val pause: () -> Unit,
    val seekTo: (Long) -> Unit,
    val trackDuration: Long?,
) {
    val isReady: Boolean
        get() = trackDuration != null

    fun requireExoPlayer() = checkNotNull(audioPlayer?.exoPlayer)
}

@Composable
fun rememberAudioPlayerState(
    trackItem: TrackItem,
    onTrackEnd: (() -> Unit)? = null
): AudioPlayerState {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val isInPreviewMode = LocalInspectionMode.current
    var currentPosition by remember { mutableLongStateOf(0L) }
    var trackDuration by remember { mutableLongStateOf(0L)  }
    var isPlaying by remember { mutableStateOf(false) }

    val player = remember {
        AudioPlayer(
            context = context,
            onTrackEnded = { onTrackEnd?.invoke() },
            onIsPlayingChange = {
                isPlaying = it
            },
            exoPlayer = if (isInPreviewMode) {
                null
            } else {
                ExoPlayer.Builder(context).build()
            },
            onPlayerReady = { duration ->
                trackDuration = duration
            },
            onPositionChange = { newPosition ->
                currentPosition = newPosition
            }
        )
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        val observer = object: DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                player.release()
                super.onDestroy(owner)
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
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
        audioPlayer = player,
        isPlaying = isPlaying,
        currentPosition = currentPosition,
        play = { player.play() },
        pause = { player.pause() },
        seekTo = {
            currentPosition = it
            player.seekTo(currentPosition)
        },
        trackDuration = trackDuration
    )
}
