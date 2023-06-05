package com.shadowflight.core.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.ui.components.AppTopBar

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
    uiState.article?.let { article ->
        ArticleScreen(navigateUp, article)
    }
}

@Composable
fun ArticleScreen(navigateUp: () -> Unit, article: Article) {
    AppTopBar(title = article.lead, navigateUp = navigateUp)
}