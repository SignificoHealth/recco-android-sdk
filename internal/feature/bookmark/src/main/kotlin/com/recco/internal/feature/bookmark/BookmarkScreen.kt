package com.recco.internal.feature.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppRecommendationCard
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
internal fun BookmarkRoute(
    navigateToArticle: (ContentId) -> Unit,
    navigateUp: () -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    BookmarkScreen(
        uiState = uiState,
        onUserInteract = { viewModel.onUserInteract(it) },
        navigateToArticle = navigateToArticle,
        navigateUp = navigateUp
    )
}

@Composable
private fun BookmarkScreen(
    uiState: UiState<BookmarkUI>,
    onUserInteract: (BookmarkUserInteract) -> Unit,
    navigateUp: () -> Unit,
    navigateToArticle: (ContentId) -> Unit,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues()
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.recco_bookmarks_title),
                navigationIcon = { BackIconButton(onClick = navigateUp) }
            )
        },
        backgroundColor = AppTheme.colors.background,
        contentPadding = contentPadding
    ) { innerPadding ->
        AppScreenStateAware(
            contentPadding = innerPadding,
            enablePullToRefresh = true,
            uiState = uiState,
            isEmpty = uiState.data?.recommendations.orEmpty().isEmpty(),
            retry = { onUserInteract(BookmarkUserInteract.Retry) },
            refresh = { onUserInteract(BookmarkUserInteract.Refresh) },
            emptyContent = {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppSpacing.dp_32),
                        text = stringResource(id = R.string.recco_bookmarks_empty_state_title),
                        style = AppTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppSpacing.dp_40),
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.recco_ic_bookmark_empty_state),
                        contentDescription = null
                    )
                }
            }
        ) { data ->
            BookmarksContent(
                feedUI = data,
                navigateToArticle = navigateToArticle
            )
        }
    }
}

@Composable
private fun BookmarksContent(
    feedUI: BookmarkUI,
    navigateToArticle: (ContentId) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(
                horizontal = AppSpacing.dp_16,
                vertical = AppSpacing.dp_24
            ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.dp_16),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_8),
            columns = GridCells.Fixed(3),
        ) {
            items(feedUI.recommendations) { recommendation ->
                AppRecommendationCard(recommendation, navigateToArticle, applyViewedOverlay = false)
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun Preview(
    @PreviewParameter(BookmarkUIPreviewProvider::class) uiState: UiState<BookmarkUI>
) {
    AppTheme {
        BookmarkScreen(
            uiState = uiState,
            onUserInteract = {},
            navigateToArticle = {},
            navigateUp = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun PreviewDark(
    @PreviewParameter(BookmarkUIPreviewProvider::class) uiState: UiState<BookmarkUI>
) {
    AppTheme(darkTheme = true) {
        BookmarkScreen(
            uiState = uiState,
            onUserInteract = {},
            navigateToArticle = {},
            navigateUp = {}
        )
    }
}