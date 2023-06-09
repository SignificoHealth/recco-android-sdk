package com.shadowflight.core.article

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.ui.components.UiState
import com.shadowflight.core.ui.preview.ArticlePreviewProvider

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