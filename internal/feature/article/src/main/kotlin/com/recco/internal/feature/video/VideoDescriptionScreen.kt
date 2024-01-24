package com.recco.internal.feature.video

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.ASPECT_RATIO_4_3
import com.recco.internal.core.ui.components.AppAsyncImage
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

// TODO, remove
val dummyVideo = Video(
    id = ContentId(
        itemId = "corrumpit",
        catalogId = "enim"
    ),
    rating = Rating.LIKE,
    status = Status.NO_INTERACTION,
    isBookmarked = false,
    videoUrl = "http://www.bing.com/search?q=indoctum",
    headline = "vidisse",
    imageUrl = null,
    disclaimer = "This exercise is not for you if you have a heart condition. Consult with your doctor before engaging in heavy cardio exercise",
    imageAlt = null,
    description = "Long description",
    length = 10
)

@Composable
fun VideoDescriptionRoute(
    navigateUp: () -> Unit,
) {
    val ui = VideoDescriptionUI(dummyVideo)

    VideoDescriptionScreen(
        navigateUp = {},
        uiState = UiState(isLoading = false, error = null, data = ui)
    )
}

@Composable
fun VideoDescriptionScreen(
    navigateUp: () -> Unit,
    uiState: UiState<VideoDescriptionUI>,
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
                        data = it.video.imageUrl,
                        alt = it.video.imageAlt,
                        contentScale = ContentScale.Crop
                    )

                    PlayButton(
                        modifier = Modifier.align(Alignment.Center),
                        onPLayButtonClick = {}
                    )

                }
            },
            retry = {  }
        ) { it ->
            CardDescriptionContent(it.video)
        }
    }
}

@Composable
private fun PlayButton(
    modifier: Modifier = Modifier,
    onPLayButtonClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = Color.DarkGray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(AppSpacing.dp_8)
    ) {
        IconButton(
            onClick = onPLayButtonClick,
            modifier = Modifier
                .background(
                    color = AppTheme.colors.accent,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(
                    horizontal = AppSpacing.dp_24,
                    vertical = AppSpacing.dp_8
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.recco_ic_play),
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = stringResource(id = R.string.recco_play),
            style = AppTheme.typography.h3,
            color = Color.White,
            modifier = Modifier.padding(horizontal = AppSpacing.dp_24)
        )
    }
}

@Composable
private fun CardDescriptionContent(video: Video) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .offset(y = -(AppSpacing.dp_24))
                .fillMaxSize(),
            elevation = 0.dp,
            shape = RoundedCornerShape(AppSpacing.dp_24),
            backgroundColor = AppTheme.colors.background
        ) {
            RecommendationTypeRow(video)
        }
    }
}

@Composable
private fun RecommendationTypeRow(video: Video) {
    Column(modifier = Modifier.padding(horizontal = AppSpacing.dp_16)) {
        Spacer(Modifier.height(AppSpacing.dp_32))

        Text(
            text = video.headline,
            style = AppTheme.typography.h1.copy(color = AppTheme.colors.primary)
        )

        Spacer(Modifier.height(AppSpacing.dp_24))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.recco_ic_video),
                tint = AppTheme.colors.primary,
                contentDescription = null
            )

            val contentTypeText = stringResource(id = R.string.recco_reccomendation_type_podcast)
            val minSuffixText = stringResource(id = R.string.recco_questionnaire_numeric_label_min)
            val contentTypeDurationText = buildString {
                append(contentTypeText)
                append("  • ${video.length} $minSuffixText").takeIf { video.length != null }
            }

            Text(
                text = contentTypeDurationText,
                style = AppTheme.typography.labelSmall.copy(color = AppTheme.colors.primary)
            )
        }

        Spacer(Modifier.height(AppSpacing.dp_24))

        video.disclaimer?.let {
            VideoDisclaimer(it)
            Spacer(Modifier.height(AppSpacing.dp_16))
        }

        Text(
            text = video.description ?: "",
            style = AppTheme.typography.body1
        )
    }
}

@Composable
private fun VideoDisclaimer(disclaimer: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_16),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.colors.accent20,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(AppSpacing.dp_16)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.recco_ic_information_circle),
            tint = AppTheme.colors.accent,
            contentDescription = null
        )

        Text(
            text = disclaimer,
            style = AppTheme.typography.body1
        )
    }
}

@Preview
@Composable
fun VideoDescriptionScreenPreview() {
    val ui = VideoDescriptionUI(dummyVideo)

    AppTheme {
        VideoDescriptionScreen(
            navigateUp = {},
            uiState = UiState(isLoading = false, error = null, data = ui)
        )
    }
}
