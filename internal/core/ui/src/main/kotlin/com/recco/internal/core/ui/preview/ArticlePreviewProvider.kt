package com.recco.internal.core.ui.preview

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status

class ArticlePreviewProvider {
    companion object {
        val NO_AUDIO_ARTICLE = Article(
            id = ContentIdPreviewProvider.data,
            rating = Rating.NOT_RATED,
            status = Status.NO_INTERACTION,
            isBookmarked = false,
            headline = "Some headline",
            lead = "Some lead",
            imageUrl = null,
            articleBodyHtml = """
              <p>Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
            """.trimIndent()
        )

        val AUDIO_ARTICLE = Article(
            id = ContentId(
                itemId = "platonem",
                catalogId = "lobortis"
            ),
            rating = Rating.DISLIKE,
            status = Status.VIEWED,
            isBookmarked = false,
            headline = "Some headline",
            lead = "Some lead",
            imageUrl = "http://google.es",
            imageAlt = null,
            audioUrl = "http://google.es",
            readingTimeInSeconds = 120,
            articleBodyHtml = """
              <p>Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
            """.trimIndent()
        )
    }
}
