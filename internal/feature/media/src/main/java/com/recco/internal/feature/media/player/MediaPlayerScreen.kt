@file:UnstableApi

package com.recco.internal.feature.media.player

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.recco.internal.core.media.MediaPlayerViewState
import com.recco.internal.core.media.rememberMediaPlayerStateWithLifecycle
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.AppTopBarDefaults
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.UserInteractionRecommendationCard
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.internal.feature.media.description.LoadMediaViewModel
import com.recco.internal.feature.media.description.MediaDescriptionUI
import com.recco.internal.feature.media.description.MediaDescriptionUserInteract
import com.recco.internal.feature.media.description.preview.MediaDescriptionUiPreviewProvider
import com.recco.internal.feature.rating.delegates.ContentUserInteract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun FullMediaPlayerRoute(
    navigateUp: () -> Unit,
    viewModel: LoadMediaViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = UiState()
    )

    val contentInteractionState by viewModel.interactionViewState
        .collectAsStateWithLifecycle(null)

    MediaPlayerScreen(
        navigateUp = navigateUp,
        uiState = uiState,
        userInteractionState = contentInteractionState,
        onUserInteract = viewModel::onUserInteract,
        onContentUserInteract = viewModel::onContentUserInteract
    )
}

@Composable
private fun MediaPlayerScreen(
    navigateUp: () -> Unit,
    uiState: UiState<MediaDescriptionUI>,
    userInteractionState: UserInteractionRecommendation?,
    onContentUserInteract: (ContentUserInteract) -> Unit,
    onUserInteract: (MediaDescriptionUserInteract) -> Unit
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
            retry = { onUserInteract(MediaDescriptionUserInteract.Retry) },
            isFloatingFooter = true,
            footerContent = {
                userInteractionState?.let {
                    AnimatedUserInteractionRecomendationCard(
                        userInteractionRecommendation = it,
                        onContentUserInteract = onContentUserInteract,
                        isVisible = playerState?.areControlsShown == false
                    )
                }
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
            actions = { } // No actions on this screen
        )
    }
}

@Composable
private fun AnimatedUserInteractionRecomendationCard(
    isVisible: Boolean,
    userInteractionRecommendation: UserInteractionRecommendation,
    onContentUserInteract: (ContentUserInteract) -> Unit
) {
    // Added check to prevent initial animation
    val coroutineScope = rememberCoroutineScope()
    val shouldAnimate = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isVisible) {
        coroutineScope.launch {
            delay(300) // Delay slightly longer than the animation duration
            shouldAnimate.value = true
        }
    }

    if (shouldAnimate.value) {
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
                userInteraction = userInteractionRecommendation,
                toggleBookmarkState = {
                    onContentUserInteract(
                        ContentUserInteract.ToggleBookmarkState(userInteractionRecommendation.contentId)
                    )
                },
                toggleLikeState = {
                    onContentUserInteract(
                        ContentUserInteract.ToggleLikeState(userInteractionRecommendation.contentId)
                    )
                },
                toggleDislikeState = {
                    onContentUserInteract(
                        ContentUserInteract.ToggleDislikeState(userInteractionRecommendation.contentId)
                    )
                }
            )
        }
    }
}

@Composable
fun MediaPlayerContent(
    playerState: MediaPlayerViewState,
    mediaDescriptionUi: MediaDescriptionUI
) {
    when (mediaDescriptionUi) {
        is MediaDescriptionUI.AudioDescriptionUI -> {
            AudioPlayerContent(
                playerState = playerState,
                mediaDescriptionUi.audio
            )
        }
        is MediaDescriptionUI.VideoDescriptionUI -> {
            VideoPlayerContent(playerState = playerState)
        }
    }
}

@Composable
private fun VideoPlayerContent(
    playerState: MediaPlayerViewState
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
            modifier = Modifier.align(Alignment.Center)
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
    audio: Audio
) {
    Box {
        val coroutineScope = rememberCoroutineScope()

        MediaPlayer(
            playerState = playerState,
            modifier = Modifier.align(Alignment.Center)
        )

        AnimatedVisibility(
            visible = playerState.areControlsShown || !playerState.isPlaying,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AudioHeader(audio)
        }

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
        // PlayerView does not work well on compose previews.
        // Show a whole size dark box instead.
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        )
    }
}

@Composable
private fun AudioHeader(audio: Audio) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.dp_16),
        modifier = Modifier.fillMaxWidth()
    ) {
        val minSuffix = stringResource(id = R.string.recco_unit_min)
        val podcastString = stringResource(id = R.string.recco_reccomendation_type_podcast)

        Spacer(modifier = Modifier.height(AppTopBarDefaults.Height))

        Text(
            text = audio.headline,
            style = AppTheme.typography.h1.copy(color = Color.White),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = AppSpacing.dp_16)
        )

        val audioDurationLabel = remember {
            val minutesLength = audio.lengthInSeconds?.div(60)
            buildString {
                append(podcastString)
                append("• 1-$minutesLength $minSuffix").takeIf { minutesLength != null }
            }
        }

        Text(
            text = audioDurationLabel,
            style = AppTheme.typography.labelSmall.copy(color = Color.White)
        )
    }
}

@Composable
private fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = {
            onClick.invoke()
        },
        backgroundColor = AppTheme.colors.accent,
        modifier = modifier
            .size(72.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (!isPlaying) {
                    R.drawable.recco_ic_play
                } else {
                    R.drawable.recco_ic_pause
                }
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
    uiState: UiState<MediaDescriptionUI>
) {
    AppTheme {
        MediaPlayerScreen(
            navigateUp = {},
            uiState = uiState,
            userInteractionState = UserInteractionRecommendation(
                contentId = ContentId("", ""),
                rating = Rating.DISLIKE
            ),
            onUserInteract = {},
            onContentUserInteract = {}
        )
    }
}
