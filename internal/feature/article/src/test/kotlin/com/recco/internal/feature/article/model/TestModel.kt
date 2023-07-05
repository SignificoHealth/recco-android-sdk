package com.recco.internal.feature.article.model

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.feature.article.ArticleUI
import com.recco.internal.feature.article.UserInteractionRecommendation


/**
 * Article
 */
internal val contentId = ContentId("testItemId", "testCatalogId")

internal val rawArticle = Article(
    id = contentId,
    rating = Rating.NOT_RATED,
    status = Status.VIEWED,
    isBookmarked = true,
    headline = "testHeadline",
    lead = "testLead",
    imageUrl = "https://test.image.url",
    articleBodyHtml = "testArticleBody"
)

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

/**
 * UiState
 */
internal fun expectedUiStateWithData(articleUI: ArticleUI) = UiState(
    isLoading = false,
    data = articleUI,
    error = null
)

internal val expectedWithLoading = UiState(
    isLoading = true,
    data = null,
    error = null
)

internal val staticThrowableForTesting = IllegalStateException()
internal val expectedWithError = UiState(
    isLoading = false,
    data = null,
    error = staticThrowableForTesting
)