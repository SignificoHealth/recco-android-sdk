package com.recco.internal.feature.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
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
    imageAlt = null,
    description = "Long description",
    length = null
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
                AppAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ASPECT_RATIO_4_3),
                    data = it.video.imageUrl,
                    alt = it.video.imageAlt,
                    contentScale = ContentScale.Crop
                )
            },
            retry = {  }
        ) { it ->
            CardDescriptionContent(it.video)
        }
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
                        painter = painterResource(id = com.recco.internal.core.ui.R.drawable.recco_ic_video),
                        tint = AppTheme.colors.primary,
                        contentDescription = null
                    )

                    Text(
                        text = "Video • 5 min",
                        style = AppTheme.typography.labelSmall.copy(color = AppTheme.colors.primary)
                    )
                }

                Spacer(Modifier.height(AppSpacing.dp_24))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_16),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .background(
                            color = AppTheme.colors.accent20,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(AppSpacing.dp_16)
                ) {
                    Icon(
                        painter = painterResource(id = com.recco.internal.core.ui.R.drawable.recco_ic_information_circle),
                        tint = AppTheme.colors.accent,
                        contentDescription = null
                    )

                    Text(
                        text = "This exercise is not for you if you have a heart condition. Consult with your doctor before engaging in heavy cardio exercise",
                            style = AppTheme.typography.body1
                    )
                }

                Spacer(Modifier.height(AppSpacing.dp_16))


                Text(
                    text = video.description ?: "",
                    style = AppTheme.typography.body1
                )
            }
        }

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
