package com.recco.internal.feature.article

import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.ui.preview.ArticlePreviewProvider

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

private val rawArticle = ArticlePreviewProvider.data()

internal fun createArticleUiGivenContent(
    rating: Rating = Rating.NOT_RATED,
    status: Status = Status.NO_INTERACTION,
    isBookmarked: Boolean = true
) = ArticleUI(
    article = createArticle(rating, status, isBookmarked),
    userInteraction = genericUserInteraction
)

internal fun createArticle(
    rating: Rating = Rating.NOT_RATED,
    status: Status = Status.NO_INTERACTION,
    isBookmarked: Boolean = true
) = rawArticle.copy(rating = rating, status = status, isBookmarked = isBookmarked)