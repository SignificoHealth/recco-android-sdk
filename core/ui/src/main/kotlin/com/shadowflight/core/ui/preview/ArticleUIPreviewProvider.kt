package com.shadowflight.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.ui.models.UiState
import com.shadowflight.core.ui.models.article.ArticleUI
import com.shadowflight.core.ui.models.article.UserInteractionRecommendation

class ArticleUIPreviewProvider :
    PreviewParameterProvider<UiState<ArticleUI>> {
    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = ArticleUI(
                    article = data(),
                    userInteraction = UserInteractionRecommendation(
                        rating = Rating.LIKE
                    )
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )

    companion object {
        fun data(
            rating: Rating = Rating.NOT_RATED,
            status: Status = Status.NO_INTERACTION,
            bookmarked: Boolean = false
        ) = Article(
            id = ContentPreviewProvider.data,
            rating = rating,
            status = status,
            isBookmarked = bookmarked,
            headline = "Some headline",
            lead = "Some lead",
            imageUrl = null,
            articleBodyHtml = "<p>Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>"
        )
    }
}