@file:UnstableApi

package com.recco.internal.core.media

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.recco.internal.core.model.media.Video

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

