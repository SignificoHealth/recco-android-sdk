package com.recco.internal.feature.article.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.preview.ArticlePreviewProvider
import com.recco.internal.feature.article.ArticleUI

internal class ArticleUIPreviewProvider :
    PreviewParameterProvider<UiState<ArticleUI>> {
    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = ArticleUI(article = ArticlePreviewProvider.NO_AUDIO_ARTICLE)
            ),
            UiState(
                isLoading = false,
                data = ArticleUI(article = ArticlePreviewProvider.AUDIO_ARTICLE)
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )
}
