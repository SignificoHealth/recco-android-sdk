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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.recco.internal.core.model.recommendation.TrackItem
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class MediaPlayerState(
    val isPlaying: Boolean,
    val playerView: PlayerView,
    val play: () -> Unit,
    val pause: () -> Unit,
)

@Composable
fun rememberMediaPlayerStateWithLifecycle(trackItem: TrackItem): MediaPlayerState {
    val context = LocalContext.current
    val isInPreviewMode = LocalInspectionMode.current
    var currentPosition by remember { mutableLongStateOf(0L) }

    val exoPlayer: ExoPlayer? = remember {
        if (!isInPreviewMode) {
            val player = ExoPlayer.Builder(context).build()
            player.setMediaItem(trackItem.asMediaItem())
            player.prepare()
            player
        } else {
            null
        }
    }

    val playerView = remember {
        val playerView = PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = false
            defaultArtwork = null
            artworkDisplayMode = PlayerView.ARTWORK_DISPLAY_MODE_FILL
        }

        loadArtworkWithCoil(context, trackItem, playerView)

        playerView
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
        isPlaying = isPlayingState,
        playerView = playerView,
        play = {
            exoPlayer?.play()
        }
    ) {
        exoPlayer?.pause()
    }
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

