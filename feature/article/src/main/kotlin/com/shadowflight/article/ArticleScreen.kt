package com.shadowflight.article

import androidx.compose.runtime.Composable
import com.shadowflight.uicommons.components.AppTopBar

@Composable
internal fun ArticleRoute(articleId: String, navigateUp: () -> Unit) {
    ArticleScreen(articleId, navigateUp)
}

@Composable
fun ArticleScreen(articleId: String, navigateUp: () -> Unit) {
    AppTopBar(title = "Article id: $articleId", navigateUp = navigateUp)
}