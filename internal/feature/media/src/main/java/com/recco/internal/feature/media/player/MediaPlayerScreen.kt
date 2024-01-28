@file:UnstableApi package com.recco.internal.feature.media.player

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.recco.internal.core.media.MediaPlayerViewState
import com.recco.internal.core.media.rememberMediaPlayerStateWithLifecycle
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.AppTopBarDefaults
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.UserInteractionRecommendation
import com.recco.internal.core.ui.components.UserInteractionRecommendationCard
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.internal.feature.media.description.MediaDescriptionUi
import com.recco.internal.feature.media.description.MediaDescriptionViewModel
import com.recco.internal.feature.media.description.preview.MediaDescriptionUiPreviewProvider
import kotlinx.coroutines.launch

@Composable
internal fun FullMediaPlayerRoute(
    navigateUp: () -> Unit,
    viewModel: MediaDescriptionViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = UiState()
    )

    FullMediaPlayerScreen(
        navigateUp = navigateUp,
        uiState = uiState,
        onUserInteract = {
        }
    )
}

@Composable
private fun FullMediaPlayerScreen(
    navigateUp: () -> Unit,
    uiState: UiState<MediaDescriptionUi>,
    onUserInteract: (MediaPlayerUserInteract) -> Unit,
) {
    val playerState = uiState.data?.trackItem?.let { trackItem ->
        rememberMediaPlayerStateWithLifecycle(trackItem)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        AppScreenStateAware(
            uiState = uiState,
            retry = { onUserInteract(MediaPlayerUserInteract.Retry) },
            isFloatingFooter = true,
            footerContent = {
                AnimatedUserInteractionReccomendationCard(
                    playerState?.isPlaying == false
                )
            }

        ) {
            playerState?.let { state ->
                uiState.data?.let { mediaDescriptionUi ->
                    MediaPlayerContent(
                        playerState = state,
                        mediaDescriptionUi = mediaDescriptionUi
                    )
                }
            }
        }

        AppTopBar(
            title = null,
            elevation = 0.dp,
            navigationIcon = {
                BackIconButton(
                    onClick = navigateUp,
                    iconTint = Color.White
                )
            },
            backgroundColor = Color.Transparent,
            actions = { },
        )
    }
}

@Composable
private fun AnimatedUserInteractionReccomendationCard(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight / 10 },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        UserInteractionRecommendationCard(
            modifier = Modifier.padding(bottom = AppSpacing.dp_24),
            userInteraction = UserInteractionRecommendation(
                rating = Rating.DISLIKE,
                isBookmarked = false,
                isBookmarkLoading = false,
                isLikeLoading = false,
                isDislikeLoading = false
            ),
            toggleBookmarkState = { TODO() },
            toggleLikeState = { TODO() },
            toggleDislikeState = { TODO() }
        )
    }
}

@Composable
fun MediaPlayerContent(
    playerState: MediaPlayerViewState,
    mediaDescriptionUi: MediaDescriptionUi
) {

    when (mediaDescriptionUi) {
        is MediaDescriptionUi.AudioDescriptionUi -> {
            AudioPlayerContent(playerState = playerState)
        }
        is MediaDescriptionUi.VideoDescriptionUi -> {
            VideoPlayerContent(playerState = playerState)
        }
    }
}

@Composable
private fun VideoPlayerContent(
    playerState: MediaPlayerViewState,
) {
    Box(modifier = Modifier.background(Color.Black)) {
        MediaPlayer(
            playerState = playerState,
            modifier = Modifier.align(Alignment.Center)
        )

        AnimatedVisibility(
            visible = !playerState.isPlaying,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            PlayButton(
                isPlaying = false,
                onClick = {
                    playerState.play()
                    playerState.playerView?.hideController()
                }
            )

        }
    }
}

@Composable
private fun AudioPlayerContent(
    playerState: MediaPlayerViewState,
) {
    Box {
        var areControlsShown by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(playerState.playerView) {
            scope.launch {
                playerState.playerView?.setControllerVisibilityListener(
                    PlayerView.ControllerVisibilityListener { visibility ->
                        areControlsShown = visibility == View.VISIBLE
                    })
            }
        }

        MediaPlayer(
            playerState = playerState,
            modifier = Modifier.align(Alignment.Center)
        )

        AnimatedVisibility(
            visible = areControlsShown || !playerState.isPlaying,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AudioHeader()
        }

        val coroutineScope = rememberCoroutineScope()

        PlayButton(
            modifier = Modifier.align(Alignment.Center),
            isPlaying = playerState.isPlaying,
            onClick = {
                if (!playerState.isPlaying) {
                    playerState.play()
                } else {
                    playerState.pause()
                }
                coroutineScope.launch {
                    playerState.playerView?.showController()
                }


            }
        )
    }
}

@Composable
private fun MediaPlayer(
    playerState: MediaPlayerViewState,
    modifier: Modifier = Modifier
) {
    val isInPreviewMode = LocalInspectionMode.current

    if (!isInPreviewMode) {
        playerState.playerView?.let { playerView ->
            AndroidView(
                factory = { playerView },
                modifier = modifier.fillMaxSize()
            )
        }
    } else {
        Box(modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
        )
    }
}

@Composable
private fun AudioHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.dp_16),
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(AppTopBarDefaults.Height))

        Text(
            text = "Sleeping like a baby",
            style = AppTheme.typography.h1.copy(color = Color.White),
        )

        Text(
            text = "Audio • 1-5min",
            style = AppTheme.typography.labelSmall.copy(color = Color.White),
        )
    }
}

@Composable
private fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = {
            onClick.invoke()
        },
        backgroundColor = AppTheme.colors.accent,
        modifier = modifier
            .size(72.dp),
    ) {
        Icon(
            painter = painterResource(
                id = if (!isPlaying)
                    R.drawable.recco_ic_play
                else
                    R.drawable.recco_ic_pause
            ),
            tint = Color.White,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun MediaScreenPreview(
    @PreviewParameter(MediaDescriptionUiPreviewProvider::class)
    uiState: UiState<MediaDescriptionUi>
) {
    AppTheme {
        FullMediaPlayerScreen(
            navigateUp = {},
            uiState = uiState,
            onUserInteract = {}
        )
    }
}
