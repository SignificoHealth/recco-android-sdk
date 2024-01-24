@file:UnstableApi package com.recco.internal.feature.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.recco.internal.core.media.rememberVideoPlayerStateWithLifecycle
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.AppTopBarDefaults
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun FullAudioPLayerRoute(
    navigateUp: () -> Unit,
    viewModel: FullAudioPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = UiState()
    )

    FullAudioPlayerScreen(
        navigateUp = navigateUp,
        uiState = uiState,
        onUserInteract = viewModel::onUserInteract
    )
}


@Composable
private fun FullAudioPlayerScreen(
    navigateUp: () -> Unit,
    uiState: UiState<FullPlayerUI>,
    onUserInteract: (FullAudioPlayerUserInteract) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        AppScreenStateAware(
            uiState = uiState,
            retry = { onUserInteract(FullAudioPlayerUserInteract.Retry) },
        ) {
            Box {
                val playerState = rememberVideoPlayerStateWithLifecycle(video = dummyVideo)

//                AppAsyncImage(
//                    modifier = Modifier.fillMaxSize(),
//                    data = "https://rec-directus.stg.significo.dev/assets/fefcd6a3-fb6a-4f76-afe0-dd816ce5f370",
//                    contentScale = ContentScale.Crop
//                )

                AndroidView(
                    factory = { ctx ->
                        playerState.playerView
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .fillMaxHeight() // Set your desired height
                )

//                Header()

                LaunchedEffect(key1 = Unit) {
                    playerState.playerView.hideController()
                }


                val coroutineScope = rememberCoroutineScope()
                PlayButton(
                    onClick = {
                        playerState.play()

                        coroutineScope.launch {
                            delay(1_500)
                            playerState.playerView.showController()
                        }
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
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
private fun Header() {
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPlayButton by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = showPlayButton,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = {
                onClick.invoke()
                showPlayButton = false
            },
            backgroundColor = AppTheme.colors.accent,
            modifier = Modifier
                .size(72.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.recco_ic_play),
                tint = Color.White,
                contentDescription = null
            )
        }
    }
}


@Preview
@Composable
private fun VideoScreenPreview() {
    val dummyAudio = Audio(
        id = ContentId(
            itemId = "corrumpit",
            catalogId = "enim"
        ),
        rating = Rating.LIKE,
        status = Status.NO_INTERACTION,
        isBookmarked = false,
        audioUrl = "http://www.bing.com/search?q=indoctum",
        headline = "vidisse",
        imageUrl = null,
        imageAlt = null,
        length = null
    )

    AppTheme {
        FullAudioPlayerScreen(
            navigateUp = {},
            uiState = UiState(
                isLoading = false,
                error = null,
                data = FullPlayerUI.AudioUi(dummyAudio)
            ),
            onUserInteract = {}
        )
    }
}
