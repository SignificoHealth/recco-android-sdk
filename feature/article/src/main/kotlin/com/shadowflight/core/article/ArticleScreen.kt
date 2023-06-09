package com.shadowflight.core.article

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.insets.ui.Scaffold
import com.ireward.htmlcompose.HtmlText
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.ui.ASPECT_RATIO_4_3
import com.shadowflight.core.ui.components.AppScreenStateAware
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.components.BackIconButton
import com.shadowflight.core.ui.extensions.isEndReached
import com.shadowflight.core.ui.extensions.openUrlInBrowser
import com.shadowflight.core.ui.models.UiState
import com.shadowflight.core.ui.models.article.ArticleUI
import com.shadowflight.core.ui.models.article.UserInteractionRecommendationCard
import com.shadowflight.core.ui.preview.ArticleUIPreviewProvider
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme

@Composable
internal fun ArticleRoute(
    navigateUp: () -> Unit,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = UiState()
    )
    val context = LocalContext.current

    ArticleScreen(
        linkClicked = { context.openUrlInBrowser(it) },
        uiState = uiState,
        navigateUp = navigateUp,
        onUserInteract = { viewModel.onUserInteract(it) }
    )
}

@Composable
private fun ArticleScreen(
    linkClicked: (String) -> Unit,
    uiState: UiState<ArticleUI>,
    navigateUp: () -> Unit,
    onUserInteract: (ArticleUserInteract) -> Unit,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues()
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = uiState.data?.article?.headline.orEmpty(),
                navigationIcon = { BackIconButton(onClick = navigateUp) }
            )
        },
        backgroundColor = AppTheme.colors.background,
        contentPadding = contentPadding
    ) { innerPadding ->
        AppScreenStateAware(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            scrollState = scrollState,
            uiState = uiState,
            retry = { onUserInteract(ArticleUserInteract.Retry) },
            animatedContent = {
                AsyncImage(
                    model = uiState.data?.article?.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ASPECT_RATIO_4_3),
                    contentScale = ContentScale.Crop
                )
            },
            isFloatingFooter = true,
            footerContent = {
                UserInteractionRecommendationCard(
                    modifier = Modifier.padding(bottom = AppSpacing.dp_24),
                    isScrollEndReached = scrollState.isEndReached(),
                    userInteraction = it.userInteraction,
                    toggleBookmarkState = { onUserInteract(ArticleUserInteract.ToggleBookmarkState) },
                    toggleLikeState = { onUserInteract(ArticleUserInteract.ToggleLikeState) },
                    toggleDislikeState = { onUserInteract(ArticleUserInteract.ToggleDislikeState) }
                )
            }
        ) {
            ArticleContent(
                linkClicked = linkClicked,
                article = it.article,
            )
        }
    }
}

@Composable
private fun ArticleContent(
    linkClicked: (String) -> Unit,
    article: Article,
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ArticleUIPreviewProvider::class) uiState: UiState<ArticleUI>
) {
    ArticleScreen(linkClicked = {}, uiState = uiState, navigateUp = { }, onUserInteract = {})
}
