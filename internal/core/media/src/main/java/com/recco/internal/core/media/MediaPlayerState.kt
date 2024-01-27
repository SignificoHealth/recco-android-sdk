@file:UnstableApi

package com.recco.internal.core.media

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class MediaPlayerState(
    val isPlaying: Boolean,
    val currentPositionMs: Long,
    val playerView: PlayerView,
    val play: () -> Unit,
)

@Composable
fun rememberMediaPlayerStateWithLifecycle(media: Any): MediaPlayerState {
    val mediaItem = (media as? Video)?.asMediaItem() ?: (media as? Audio)?.asMediaItem() ?:
        error("media ($media) must be a ${Video::class} or ${Audio::class}")
    val context = LocalContext.current
    val isInPreviewMode = LocalInspectionMode.current
    var currentPosition by remember { mutableLongStateOf(0L) }

    val exoPlayer: ExoPlayer? = remember {
        if (!isInPreviewMode) {
            val player = ExoPlayer.Builder(context).build()
            player.setMediaItem(mediaItem)
            player.prepare()
            player
        } else {
            null
        }
    }

    val playerView = remember {
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()

        PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = false
        }
    }
    val lifecycleObserver = rememberPlayerLifecycleObserver(playerView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var isPlayingState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isPlayingState) {
        currentPosition = exoPlayer?.currentPosition?.coerceAtLeast(0) ?: 0

        while(isPlayingState) {
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
        })
    }

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return MediaPlayerState(
        playerView = playerView,
        isPlaying = isPlayingState,
        currentPositionMs = currentPosition,
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

