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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppAlertDialog
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.AppTopBarDefaults
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.UserInteractionRecommendationCard
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.internal.feature.media.description.MediaDescriptionUI
import com.recco.internal.feature.media.description.MediaDescriptionUserInteract
import com.recco.internal.feature.media.description.MediaViewModel
import com.recco.internal.feature.media.description.preview.MediaDescriptionUiPreviewProvider
import com.recco.internal.feature.rating.delegates.ContentUserInteract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun FullMediaPlayerRoute(
    navigateUp: () -> Unit,
    viewModel: MediaViewModel = hiltViewModel()
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
    Box(
        modifier = Modifier
            .background(AppTheme.colors.background)
    ) {
        val playerState = uiState.data?.let {
            rememberMediaPlayerStateWithLifecycle(it.trackItem)
        }

        AppScreenStateAware(
            uiState = uiState,
            retry = { onUserInteract(MediaDescriptionUserInteract.Retry) },
            shouldFillMaxSize = false,
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
            uiState.data?.let { mediaDescriptionUi ->
                playerState?.let { state ->
                    MediaPlayerContent(
                        mediaPlayerViewState = state,
                        mediaDescriptionUi = mediaDescriptionUi,
                        onUserInteract = onUserInteract
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

    LaunchedEffect(isVisible) {
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
                        ContentUserInteract.ToggleBookmarkState(
                            userInteractionRecommendation.contentId,
                            userInteractionRecommendation.contentType

                        )
                    )
                },
                toggleLikeState = {
                    onContentUserInteract(
                        ContentUserInteract.ToggleLikeState(
                            userInteractionRecommendation.contentId,
                            userInteractionRecommendation.contentType
                        )
                    )
                },
                toggleDislikeState = {
                    onContentUserInteract(
                        ContentUserInteract.ToggleDislikeState(
                            userInteractionRecommendation.contentId,
                            userInteractionRecommendation.contentType
                        )
                    )
                }
            )
        }
    }
}

@Composable
internal fun MediaPlayerContent(
    mediaDescriptionUi: MediaDescriptionUI,
    mediaPlayerViewState: MediaPlayerViewState,
    onUserInteract: (MediaDescriptionUserInteract) -> Unit
) {
    when (mediaDescriptionUi) {
        is MediaDescriptionUI.AudioDescriptionUI -> {
            AudioPlayerContent(
                mediaDescriptionUi.audio,
                mediaPlayerViewState
            )
        }
        is MediaDescriptionUI.VideoDescriptionUI -> {
            VideoPlayerContent(
                playerState = mediaPlayerViewState,
                videoDescriptionUI = mediaDescriptionUi,
                onUserInteract = onUserInteract
            )
        }
    }
}

@Composable
private fun VideoPlayerContent(
    playerState: MediaPlayerViewState,
    videoDescriptionUI: MediaDescriptionUI.VideoDescriptionUI,
    onUserInteract: (MediaDescriptionUserInteract) -> Unit
) {
    var openWarningDialog = remember { mutableStateOf(false) }

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
                    if (videoDescriptionUI.shouldShowWarningDialog) {
                        openWarningDialog.value = true
                    } else {
                        playerState.play()
                        playerState.playerView?.hideController()
                    }
                }
            )
        }

        VideoWarningDialog(openWarningDialog, onUserInteract)
    }
}

@Composable
private fun VideoWarningDialog(
    openWarningDialog: MutableState<Boolean>,
    onUserInteract: (MediaDescriptionUserInteract) -> Unit
) {
    var isDontShowAnymoreChecked by remember { mutableStateOf(false) }

    AppAlertDialog(
        openDialog = openWarningDialog,
        titleRes = R.string.recco_warning,
        textButtonPrimaryRes = R.string.recco_dismiss,
        content = {
            Spacer(modifier = Modifier.height(AppSpacing.dp_16))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.recco_warning_message),
                style = AppTheme.typography.body3
            )

            Spacer(modifier = Modifier.height(AppSpacing.dp_16))

            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.dp_16)
            ) {
                Checkbox(
                    checked = isDontShowAnymoreChecked,
                    onCheckedChange = { isChecked ->
                        isDontShowAnymoreChecked = isChecked
                        onUserInteract(MediaDescriptionUserInteract.DontShowWarningDialogChecked(isChecked))
                    }
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.recco_warning_dismiss),
                    style = AppTheme.typography.body2
                )
            }
        },
        onDismiss = {
            onUserInteract(MediaDescriptionUserInteract.OnWarningDialogDismiss)
        },
        onClickPrimary = {}
    )
}

@Composable
private fun AudioPlayerContent(
    audio: Audio,
    playerState: MediaPlayerViewState
) {
    Box {
        MediaPlayer(
            playerState = playerState,
            modifier = Modifier
                .align(Alignment.Center)
                .background(AppTheme.colors.staticDark)
        )

        AnimatedVisibility(
            visible = playerState.areControlsShown || !playerState.isPlaying,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AudioHeader(audio)
        }

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
        val podcastString = stringResource(id = R.string.recco_reccomendation_type_audio)

        Spacer(modifier = Modifier.height(AppTopBarDefaults.Height))

        Text(
            text = audio.headline,
            style = AppTheme.typography.h1.copy(color = Color.White),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = AppSpacing.dp_16)
        )

        val audioDurationLabel = remember {
            val minutesLength = audio.lengthInSeconds
                ?.takeIf { it > 0 }
                ?.div(60)

            buildString {
                append(podcastString)
                minutesLength?.let { append("• 1-$minutesLength $minSuffix") }
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
                rating = Rating.DISLIKE,
                contentType = ContentType.AUDIO
            ),
            onUserInteract = {},
            onContentUserInteract = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaScreenWithDialogPreview(
    @PreviewParameter(MediaDescriptionUiPreviewProvider::class)
    uiState: UiState<MediaDescriptionUI>
) {
    val openDialog = remember {
        mutableStateOf(true)
    }
    AppTheme {
        VideoWarningDialog(
            openWarningDialog = openDialog,
            onUserInteract = {}
        )
    }
}
