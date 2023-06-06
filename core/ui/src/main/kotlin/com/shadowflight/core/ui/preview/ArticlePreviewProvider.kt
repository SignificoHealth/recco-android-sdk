package com.shadowflight.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Status

class ArticlePreviewProvider :
    PreviewParameterProvider<Article> {
    override val values
        get() = sequenceOf(data())

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