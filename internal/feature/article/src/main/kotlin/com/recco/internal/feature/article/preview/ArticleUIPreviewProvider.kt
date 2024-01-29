package com.recco.internal.feature.article.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.preview.ArticlePreviewProvider
import com.recco.internal.feature.article.ArticleUI

internal class ArticleUIPreviewProvider :
    PreviewParameterProvider<UiState<ArticleUI>> {
    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = ArticleUI(
                    article = ArticlePreviewProvider.NO_AUDIO_ARTICLE,
                    userInteraction = UserInteractionRecommendation(
                        contentId = ContentId(itemId = "1", catalogId = "1"),
                        rating = Rating.LIKE
                    )
                )
            ),
            UiState(
                isLoading = false,
                data = ArticleUI(
                    article = ArticlePreviewProvider.AUDIO_ARTICLE,
                    userInteraction = UserInteractionRecommendation(
                        contentId = ContentId(itemId = "1", catalogId = "1"),
                        rating = Rating.LIKE
                    )
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )
}
