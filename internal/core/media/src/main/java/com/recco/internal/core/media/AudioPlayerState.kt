@file:UnstableApi package com.recco.internal.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.recco.internal.core.model.media.Video
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class AudioPlayerState(
    val video: Video,
    val audioPlayer: AudioPlayer?,
    val isPlaying: Boolean,
    val currentPosition: Long,
    val play: () -> Unit,
    val pause: () -> Unit,
    val seekTo: (Long) -> Unit
) {
    fun requireExoPlayer() = checkNotNull(audioPlayer?.exoPlayer)
}

class VideoPlayerState(
    val playerView: PlayerView,
    val play: () -> Unit,
)

@Composable
fun rememberVideoPlayerStateWithLifecycle(video: Video): VideoPlayerState {
    val context = LocalContext.current
    val isInPreviewMode = LocalInspectionMode.current

    val exoPlayer: ExoPlayer? = remember {
        if (!isInPreviewMode) {
            val player = ExoPlayer.Builder(context).build()
            player.setMediaItem(video.asMediaItem())
            player.prepare()
            player
        } else {
            null
        }
    }

    val playerView = remember {
        exoPlayer?.setMediaItem(video.asMediaItem())
        exoPlayer?.prepare()

        PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = false
        }
    }
    val lifecycleObserver = rememberPlayerLifecycleObserver(playerView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return VideoPlayerState(
        playerView = playerView,
        play = {
            exoPlayer?.play()
        }
    )
}

@Composable
private fun rememberPlayerLifecycleObserver(player: PlayerView): LifecycleEventObserver = remember(player) {
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> player.onResume()
            Lifecycle.Event.ON_PAUSE -> player.onPause()
            Lifecycle.Event.ON_DESTROY -> player.player?.release()
            else -> {
                // Do Nothing
            }
        }
    }
}


@Composable
fun rememberPlayerState(
    video: Video,
    onTrackEnd: (() -> Unit)? = null
): AudioPlayerState {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val isInPreviewMode = LocalInspectionMode.current
    var currentPosition by remember { mutableStateOf(0L) }
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
    LaunchedEffect(video) {
        player.load(video)
    }

    return AudioPlayerState(
        video = video,
        isPlaying = isPlaying,
        audioPlayer = player,
        currentPosition = currentPosition,
        play = { player.play() },
        pause = { player.pause() },
        seekTo = {
            currentPosition = it
            player.seekTo(currentPosition)
        }
    )
}
