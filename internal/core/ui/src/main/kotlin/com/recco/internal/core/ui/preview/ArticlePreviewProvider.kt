package com.recco.internal.core.ui.preview

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status

class ArticlePreviewProvider {
    companion object {
        fun data(
            rating: Rating = Rating.NOT_RATED,
            status: Status = Status.NO_INTERACTION,
            bookmarked: Boolean = false
        ) = Article(
            id = ContentIdPreviewProvider.data,
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