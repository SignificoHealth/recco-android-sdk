package com.recco.internal.feature.article.model

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.ui.preview.ArticlePreviewProvider
import com.recco.internal.feature.article.ArticleUI
import com.recco.internal.feature.article.UserInteractionRecommendation


internal val rawArticle = ArticlePreviewProvider.data(bookmarked = true)

internal val likedArticle = rawArticle.copy(rating = Rating.LIKE)
internal val dislikedArticle = rawArticle.copy(rating = Rating.DISLIKE)
internal val nonBookmarkedArticle = rawArticle.copy(isBookmarked = false)

/**
 * UserInteractionRecommendation
 */
internal val genericUserInteraction = UserInteractionRecommendation(
    rating = Rating.LIKE,
    isBookmarked = true
)

/**
 * ArticleUi
 */
internal fun createArticleUiGivenContent(article: Article) = ArticleUI(
    article = article,
    userInteraction = genericUserInteraction
)