package com.shadowflight.article

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
internal fun ArticleRoute(articleId: String) {
    ArticleScreen(articleId)
}

@Composable
fun ArticleScreen(articleId: String) {
    Text(text = "Article id: $articleId")
}