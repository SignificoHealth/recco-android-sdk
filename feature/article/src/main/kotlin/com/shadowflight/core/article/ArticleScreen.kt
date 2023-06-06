package com.shadowflight.core.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ireward.htmlcompose.HtmlText
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.ui.ASPECT_RATIO_4_3
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.extensions.isEndReached
import com.shadowflight.core.ui.extensions.openUrlInBrowser
import com.shadowflight.core.ui.preview.ArticlePreviewProvider
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme

@Composable
internal fun ArticleRoute(
    navigateUp: () -> Unit,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = ArticleViewUIState(
            isLoading = true
        )
    )

    val context = LocalContext.current
    uiState.article?.let { article ->
        ArticleScreen(
            navigateUp = navigateUp,
            linkClicked = { context.openUrlInBrowser(it) },
            article = article,
            userInteraction = checkNotNull(uiState.userInteraction),
            toggleBookmarkState = { viewModel.onUserInteract(ArticleUserInteract.ToggleBookmarkState) },
            toggleLikeState = { viewModel.onUserInteract(ArticleUserInteract.ToggleLikeState) },
            toggleDislikeState = { viewModel.onUserInteract(ArticleUserInteract.ToggleDislikeState) }
        )
    }
}

@Composable
fun ArticleScreen(
    navigateUp: () -> Unit,
    linkClicked: (String) -> Unit,
    article: Article,
    userInteraction: UserInteractionRecommendation,
    toggleBookmarkState: () -> Unit,
    toggleLikeState: () -> Unit,
    toggleDislikeState: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = article.lead, navigateUp = navigateUp)
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()

            ) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(ASPECT_RATIO_4_3)
                        .graphicsLayer { translationY = scrollState.value * 0.4f },
                    contentScale = ContentScale.Crop
                )
                Card(
                    modifier = Modifier
                        .offset(y = -(AppSpacing.dp_24))
                        .fillMaxSize(),
                    elevation = 0.dp,
                    shape = RoundedCornerShape(AppSpacing.dp_24),
                    backgroundColor = AppTheme.colors.background,
                ) {
                    Column(modifier = Modifier.padding(horizontal = AppSpacing.dp_16)) {
                        Spacer(Modifier.height(AppSpacing.dp_32))

                        Text(
                            text = article.headline,
                            style = AppTheme.typography.h1.copy(color = AppTheme.colors.onBackground)
                        )
                        Spacer(Modifier.height(AppSpacing.dp_32))

                        Divider(
                            color = AppTheme.colors.accent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(9.dp)
                        )
                        Spacer(Modifier.height(AppSpacing.dp_32))

                        article.lead?.let { lead ->
                            Text(
                                text = lead,
                                style = AppTheme.typography.body1Bold.copy(color = AppTheme.colors.onBackground)
                            )
                            Spacer(Modifier.height(AppSpacing.dp_32))
                        }

                        article.articleBodyHtml?.let { body ->
                            HtmlText(
                                text = body.replace("\n", "<br/>"),
                                linkClicked = linkClicked,
                                style = AppTheme.typography.body2.copy(color = AppTheme.colors.onBackground)
                            )
                        }

                        Spacer(Modifier.height(AppSpacing.dp_24 * 2))
                    }
                }
            }

            UserInteractionRecommendationCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = AppSpacing.dp_24),
                isScrollEndReached = scrollState.isEndReached(),
                userInteraction = userInteraction,
                toggleBookmarkState = toggleBookmarkState,
                toggleLikeState = toggleLikeState,
                toggleDislikeState = toggleDislikeState
            )
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ArticlePreviewProvider::class) data: Article
) {
    ArticleScreen(
        navigateUp = { },
        linkClicked = { },
        article = data,
        userInteraction = UserInteractionRecommendation(
            rating = data.rating,
            isBookmarked = data.isBookmarked
        ),
        toggleBookmarkState = { },
        toggleLikeState = { },
        toggleDislikeState = {}
    )
}
