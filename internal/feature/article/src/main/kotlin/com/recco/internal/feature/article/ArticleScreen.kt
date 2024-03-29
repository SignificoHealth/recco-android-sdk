package com.recco.internal.feature.article

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.insets.ui.Scaffold
import com.ireward.htmlcompose.HtmlText
import com.recco.internal.core.media.MediaPlayerState
import com.recco.internal.core.media.mapper.asTrackItemOrNull
import com.recco.internal.core.media.rememberAudioPlayerState
import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.ASPECT_RATIO_4_3
import com.recco.internal.core.ui.components.AppAsyncImage
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.RecommendationTypeRow
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.UserInteractionRecommendationCard
import com.recco.internal.core.ui.extensions.MeasureTextWidth
import com.recco.internal.core.ui.extensions.formatElapsedTime
import com.recco.internal.core.ui.extensions.isEndReached
import com.recco.internal.core.ui.extensions.openUrlInBrowser
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.internal.feature.article.preview.ArticleUIPreviewProvider
import com.recco.internal.feature.article.preview.ContentUserInteractionPreviewProvider
import com.recco.internal.feature.rating.delegates.ContentUserInteract

@Composable
internal fun ArticleRoute(
    navigateUp: () -> Unit,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = UiState()
    )
    val contentInteractionState by viewModel.interactionViewState
        .collectAsStateWithLifecycle(initialValue = null)

    val context = LocalContext.current

    ArticleScreen(
        linkClicked = { context.openUrlInBrowser(it) },
        uiState = uiState,
        contentInteractionState = contentInteractionState,
        navigateUp = navigateUp,
        onUserInteract = viewModel::onUserInteract,
        onContentUserInteract = viewModel::onContentUserInteract
    )
}

@Composable
private fun ArticleScreen(
    linkClicked: (String) -> Unit,
    uiState: UiState<ArticleUI>,
    navigateUp: () -> Unit,
    onContentUserInteract: (ContentUserInteract) -> Unit,
    onUserInteract: (ArticleUserInteract) -> Unit,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues(),
    contentInteractionState: UserInteractionRecommendation?
) {
    val scrollState = rememberScrollState()

    val audioPlayerState = uiState.data?.article?.asTrackItemOrNull()?.let {
        rememberAudioPlayerState(it)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = uiState.data?.article?.headline.orEmpty(),
                navigationIcon = {
                    BackIconButton(onClick = {
                        navigateUp.invoke()
                        audioPlayerState?.release?.invoke()
                    })
                }
            )
        },
        backgroundColor = AppTheme.colors.background,
        contentPadding = contentPadding
    ) { innerPadding ->
        AppScreenStateAware(
            contentPadding = innerPadding,
            scrollState = scrollState,
            uiState = uiState,
            retry = { onUserInteract(ArticleUserInteract.Retry) },
            animatedContent = {
                AppAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ASPECT_RATIO_4_3),
                    data = it.article.imageUrl,
                    alt = it.article.imageAlt,
                    contentScale = ContentScale.Crop
                )
            },
            isFloatingFooter = true,
            footerContent = {
                contentInteractionState?.let { contentInteraction ->
                    UserInteractionRecommendationCard(
                        modifier = Modifier.padding(bottom = AppSpacing.dp_24),
                        isScrollEndReached = scrollState.isEndReached(),
                        userInteraction = contentInteraction,
                        toggleBookmarkState = {
                            onContentUserInteract(
                                ContentUserInteract.ToggleBookmarkState(
                                    it.article.id,
                                    it.article.contentType
                                )
                            )
                        },
                        toggleLikeState = {
                            onContentUserInteract(
                                ContentUserInteract.ToggleLikeState(
                                    it.article.id,
                                    it.article.contentType
                                )
                            )
                        },
                        toggleDislikeState = {
                            onContentUserInteract(
                                ContentUserInteract.ToggleDislikeState(
                                    it.article.id,
                                    it.article.contentType
                                )
                            )
                        }
                    )
                }
            }
        ) {
            ArticleContent(
                linkClicked = linkClicked,
                article = it.article,
                audioPlayerState = audioPlayerState
            )
        }
    }
}

@Composable
private fun ArticleContent(
    linkClicked: (String) -> Unit,
    article: Article,
    audioPlayerState: MediaPlayerState? = null
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .offset(y = -(AppSpacing.dp_24))
                .fillMaxSize(),
            elevation = 0.dp,
            shape = RoundedCornerShape(AppSpacing.dp_24),
            backgroundColor = AppTheme.colors.background
        ) {
            Column(modifier = Modifier.padding(horizontal = AppSpacing.dp_16)) {
                Spacer(Modifier.height(AppSpacing.dp_32))

                Text(
                    text = article.headline,
                    style = AppTheme.typography.h1.copy(color = AppTheme.colors.primary)
                )
                Spacer(Modifier.height(AppSpacing.dp_16))

                if (article.hasAudio && audioPlayerState != null) {
                    RecommendationTypeRow(
                        contentType = ContentType.ARTICLE,
                        lengthInSeconds = article.readingTimeInSeconds
                    )

                    Spacer(Modifier.height(AppSpacing.dp_16))

                    AudioPlayer(
                        playerState = audioPlayerState
                    )

                    Spacer(Modifier.height(AppSpacing.dp_32))
                }

                if (!article.hasAudio) {
                    Column {
                        Divider(
                            color = AppTheme.colors.accent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                        )
                        Spacer(Modifier.height(AppSpacing.dp_32))
                    }
                }

                article.lead?.let { lead ->
                    Text(
                        text = lead,
                        style = AppTheme.typography.body1Bold.copy(color = AppTheme.colors.primary)
                    )
                    Spacer(Modifier.height(AppSpacing.dp_32))
                }

                article.articleBodyHtml?.let { body ->
                    HtmlText(
                        text = body.replace("\n", "<br/>"),
                        linkClicked = linkClicked,
                        style = AppTheme.typography.body2.copy(color = AppTheme.colors.primary)
                    )
                }

                Spacer(Modifier.height(AppSpacing.dp_24 * 2))
            }
        }
    }
}

@Composable
private fun AudioPlayer(
    playerState: MediaPlayerState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = AppSpacing.dp_32))
            .background(color = AppTheme.colors.primary10)
            .padding(horizontal = AppSpacing.dp_24, vertical = AppSpacing.dp_8)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_16)
        ) {
            Icon(
                modifier = Modifier.clickable {
                    if (playerState.isPlaying) {
                        playerState.pause()
                    } else {
                        playerState.play()
                    }
                },
                painter = painterResource(
                    id = if (playerState.isPlaying) R.drawable.recco_ic_pause else R.drawable.recco_ic_play
                ),
                tint = AppTheme.colors.primary,
                contentDescription = null
            )

            // Calculate the widest text value to prevent
            // the layout to stretch when size changes
            val timeTextWidth = MeasureTextWidth(
                text = "44:44",
                style = AppTheme.typography.labelSmall
            ) + 2.dp

            Text(
                text = playerState.currentPosition.formatElapsedTime(),
                style = AppTheme.typography.labelSmall,
                modifier = Modifier.width(timeTextWidth)
            )

            AudioSlider(
                modifier = Modifier.weight(1f),
                audioDurationMs = playerState.trackDuration ?: 0L,
                currentPositionMs = playerState.currentPosition,
                enabled = playerState.isReady,
                onSeekPosition = { playerState.seekTo(it) }
            )

            playerState.trackDuration?.let { duration ->
                Text(
                    text = duration.formatElapsedTime(),
                    style = AppTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun AudioSlider(
    modifier: Modifier,
    audioDurationMs: Long,
    currentPositionMs: Long,
    enabled: Boolean,
    onSeekPosition: (Long) -> Unit
) {
    // Convert milliseconds to seconds for Slider
    val currentPositionSeconds = currentPositionMs / 1000f
    val audioDurationSeconds = audioDurationMs / 1000f
    var sliderPosition by remember { mutableFloatStateOf(currentPositionSeconds) }
    var isDragging by remember { mutableStateOf(false) }

    // Update slider only when not dragging
    LaunchedEffect(currentPositionMs) {
        if (!isDragging) {
            sliderPosition = currentPositionSeconds
        }
    }

    Slider(
        value = sliderPosition,
        onValueChange = { newPosition ->
            sliderPosition = newPosition
            isDragging = true
        },
        onValueChangeFinished = {
            // Convert seconds back to milliseconds for accurate seeking
            onSeekPosition((sliderPosition * 1000).toLong())
            isDragging = false
        },
        valueRange = 0f..audioDurationSeconds,
        enabled = enabled,
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = AppTheme.colors.background,
            activeTrackColor = AppTheme.colors.primary,
            inactiveTrackColor = AppTheme.colors.primary.copy(alpha = 0.2f)
        )
    )
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ArticleUIPreviewProvider::class) uiState: UiState<ArticleUI>
) {
    AppTheme {
        ArticleScreen(
            linkClicked = {},
            uiState = uiState,
            navigateUp = { },
            onContentUserInteract = {},
            onUserInteract = {},
            contentInteractionState = null
        )
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ContentUserInteractionPreviewProvider::class)
    userInteractionRecommendation: UserInteractionRecommendation
) {
    AppTheme {
        ArticleScreen(
            linkClicked = {},
            uiState = ArticleUIPreviewProvider().values.first(),
            navigateUp = { },
            onContentUserInteract = {},
            onUserInteract = {},
            contentInteractionState = userInteractionRecommendation
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(ArticleUIPreviewProvider::class) uiState: UiState<ArticleUI>
) {
    AppTheme(darkTheme = true) {
        ArticleScreen(
            linkClicked = {},
            uiState = uiState,
            navigateUp = { },
            onContentUserInteract = {},
            onUserInteract = {},
            contentInteractionState = null
        )
    }
}
