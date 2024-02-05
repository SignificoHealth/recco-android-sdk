package com.recco.internal.feature.media.description

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.insets.ui.Scaffold
import com.ireward.htmlcompose.HtmlText
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.ASPECT_RATIO_4_3
import com.recco.internal.core.ui.components.AppAsyncImage
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.LargePlayButton
import com.recco.internal.core.ui.components.RecommendationTypeRow
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.UserInteractionRecommendationCard
import com.recco.internal.core.ui.extensions.isEndReached
import com.recco.internal.core.ui.lifecycle.LifecycleEffect
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.internal.feature.media.description.preview.MediaDescriptionUiPreviewProvider
import com.recco.internal.feature.rating.delegates.ContentUserInteract

@Composable
internal fun MediaDescriptionRoute(
    navigateUp: () -> Unit,
    viewModel: LoadMediaViewModel = hiltViewModel(),
    navigateToMediaPlayer: (ContentId, ContentType) -> Unit
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = UiState()
    )

    val contentInteractionState by viewModel.interactionViewState
        .collectAsStateWithLifecycle(null)

    LifecycleEffect(
        onResume = {
            viewModel.onUserInteract(MediaDescriptionUserInteract.InitialLoad)
        }
    )

    MediaDescriptionScreen(
        navigateUp = navigateUp,
        navigateToMediaPlayer = navigateToMediaPlayer,
        uiState = uiState,
        userInteractionState = contentInteractionState,
        onUserInteract = viewModel::onUserInteract,
        onContentUserInteract = viewModel::onContentUserInteract
    )
}

@Composable
private fun MediaDescriptionScreen(
    navigateUp: () -> Unit,
    navigateToMediaPlayer: (ContentId, ContentType) -> Unit,
    uiState: UiState<MediaDescriptionUI>,
    userInteractionState: UserInteractionRecommendation?,
    onUserInteract: (MediaDescriptionUserInteract) -> Unit,
    onContentUserInteract: (ContentUserInteract) -> Unit,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues()
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = null,
                navigationIcon = { BackIconButton(onClick = navigateUp) }
            )
        },
        backgroundColor = AppTheme.colors.background,
        contentPadding = contentPadding
    ) { innerPadding ->
        AppScreenStateAware(
            contentPadding = innerPadding,
            scrollState = scrollState,
            uiState = uiState,
            animatedContent = {
                Box {
                    AppAsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ASPECT_RATIO_4_3),
                        data = it.imageUrl,
                        alt = it.imageAlt,
                        contentScale = ContentScale.Crop
                    )

                    LargePlayButton(
                        modifier = Modifier.align(Alignment.Center),
                        onPlayButtonClick = {
                            uiState.data?.apply {
                                navigateToMediaPlayer(contentId, contentType)
                            }
                        }
                    )
                }
            },
            isFloatingFooter = true,
            footerContent = {
                userInteractionState?.let {
                    UserInteractionRecommendationCard(
                        modifier = Modifier.padding(bottom = AppSpacing.dp_24),
                        isScrollEndReached = scrollState.isEndReached(),
                        userInteraction = it,
                        toggleBookmarkState = {
                            onContentUserInteract(
                                ContentUserInteract.ToggleBookmarkState(it.contentId)
                            )
                        },
                        toggleLikeState = {
                            onContentUserInteract(
                                ContentUserInteract.ToggleLikeState(it.contentId)
                            )
                        },
                        toggleDislikeState = {
                            onContentUserInteract(
                                ContentUserInteract.ToggleDislikeState(it.contentId)
                            )
                        }
                    )
                }
            },
            retry = {
                onUserInteract(MediaDescriptionUserInteract.Retry)
            }
        ) {
            CardDescriptionContent(it)
        }
    }
}

@Composable
private fun CardDescriptionContent(mediaUi: MediaDescriptionUI) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .offset(y = -(AppSpacing.dp_24))
                .fillMaxSize(),
            elevation = 0.dp,
            shape = RoundedCornerShape(AppSpacing.dp_24),
            backgroundColor = AppTheme.colors.background
        ) {
            when (mediaUi) {
                is MediaDescriptionUI.AudioDescriptionUI -> {
                    AudioDescriptionContent(mediaUi.audio)
                }
                is MediaDescriptionUI.VideoDescriptionUI -> {
                    VideoDescriptionContent(mediaUi.video)
                }
            }
        }
    }
}

@Composable
private fun AudioDescriptionContent(audio: Audio) {
    Column(modifier = Modifier.padding(horizontal = AppSpacing.dp_16)) {
        Spacer(Modifier.height(AppSpacing.dp_32))

        Text(
            text = audio.headline,
            style = AppTheme.typography.h1.copy(color = AppTheme.colors.primary)
        )

        Spacer(Modifier.height(AppSpacing.dp_24))

        RecommendationTypeRow(
            contentType = ContentType.AUDIO,
            lengthInSeconds = audio.lengthInSeconds
        )

        Spacer(Modifier.height(AppSpacing.dp_24))

        if (audio.hasTranscription && audio.description != null) {
            Divider(
                color = AppTheme.colors.accent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )

            Spacer(Modifier.height(AppSpacing.dp_24))

            Text(
                text = stringResource(id = R.string.recco_transcription),
                style = AppTheme.typography.h4.copy(color = AppTheme.colors.accent)
            )

            Spacer(Modifier.height(AppSpacing.dp_24))

            HtmlText(
                text = audio.description?.replace("\n", "<br/>") ?: "",
                style = AppTheme.typography.body3.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}

@Composable
private fun VideoDescriptionContent(video: Video) {
    Column(modifier = Modifier.padding(horizontal = AppSpacing.dp_16)) {
        Spacer(Modifier.height(AppSpacing.dp_32))

        Text(
            text = video.headline,
            style = AppTheme.typography.h1.copy(color = AppTheme.colors.primary)
        )

        Spacer(Modifier.height(AppSpacing.dp_24))

        RecommendationTypeRow(
            contentType = ContentType.VIDEO,
            lengthInSeconds = video.lengthInSeconds
        )

        Spacer(Modifier.height(AppSpacing.dp_24))

        HtmlText(
            text = video.description?.replace("\n", "<br/>") ?: "",
            style = AppTheme.typography.body1.copy(color = AppTheme.colors.primary)
        )
    }
}

@Preview
@Composable
fun MediaDescriptionScreenPreview(
    @PreviewParameter(MediaDescriptionUiPreviewProvider::class) uiState: UiState<MediaDescriptionUI>
) {
    AppTheme {
        MediaDescriptionScreen(
            navigateUp = {},
            uiState = uiState,
            userInteractionState = null,
            navigateToMediaPlayer = { _, _ -> },
            onUserInteract = {},
            onContentUserInteract = {}
        )
    }
}

@Preview
@Composable
fun MediaDescriptionScreenPreviewDark(
    @PreviewParameter(MediaDescriptionUiPreviewProvider::class) uiState: UiState<MediaDescriptionUI>
) {
    AppTheme(darkTheme = true) {
        MediaDescriptionScreen(
            navigateUp = {},
            uiState = uiState,
            userInteractionState = null,
            navigateToMediaPlayer = { _, _ -> },
            onUserInteract = {},
            onContentUserInteract = {}
        )
    }
}

