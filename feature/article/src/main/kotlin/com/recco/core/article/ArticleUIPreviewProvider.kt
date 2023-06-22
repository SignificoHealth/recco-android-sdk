package com.recco.core.article

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.core.model.recommendation.Rating
import com.recco.core.ui.components.UiState
import com.recco.core.ui.preview.ArticlePreviewProvider

class ArticleUIPreviewProvider :
    PreviewParameterProvider<UiState<ArticleUI>> {
    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = ArticleUI(
                    article = ArticlePreviewProvider.data(),
                    userInteraction = UserInteractionRecommendation(
                        rating = Rating.LIKE
                    )
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )
}