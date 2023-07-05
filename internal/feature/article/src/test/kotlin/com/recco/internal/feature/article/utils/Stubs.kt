package com.recco.internal.feature.article.utils

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.feature.article.model.dislikedArticle
import com.recco.internal.feature.article.model.likedArticle
import com.recco.internal.feature.article.model.nonBookmarkedArticle
import com.recco.internal.feature.article.model.rawArticle
import com.recco.internal.feature.article.model.staticThrowableForTesting
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.stub


internal fun RecommendationRepository.stubForSuccessWithLikedAndBookmarkedArticle() {
    stubRepositoryForSuccess(likedArticle)
}

internal fun RecommendationRepository.stubForSuccessWithDislikedArticle() {
    stubRepositoryForSuccess(dislikedArticle)
}

internal fun RecommendationRepository.stubForSuccessWithNonRatedArticle() {
    stubRepositoryForSuccess(rawArticle)
}

internal fun RecommendationRepository.stubForSuccessWithNonBookmarkedArticle() {
    stubRepositoryForSuccess(nonBookmarkedArticle)
}

internal fun RecommendationRepository.stubForInitialFailure() {
    this.stub {
        onBlocking { it.getArticle(any()) } doThrow staticThrowableForTesting
        onBlocking { it.setRecommendationAsViewed(any()) } doThrow staticThrowableForTesting
    }
}

internal fun RecommendationRepository.stubForToggleRatingFailure() {
    this.stub {
        onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
        onBlocking { it.getArticle(any()) } doReturn likedArticle
        onBlocking {
            it.setRecommendationRating(
                any(),
                any()
            )
        } doThrow staticThrowableForTesting
    }
}

internal fun RecommendationRepository.stubForToggleBookmarkFailure() {
    this.stub {
        onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
        onBlocking { it.getArticle(any()) } doReturn likedArticle
        onBlocking {
            it.setBookmarkRecommendation(
                any(),
                any()
            )
        } doThrow staticThrowableForTesting
    }
}

private fun RecommendationRepository.stubRepositoryForSuccess(article: Article) {
    this.stub {
        onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
        onBlocking { it.getArticle(any()) } doReturn article
        onBlocking { it.setBookmarkRecommendation(any(), any()) } doReturn Unit
        onBlocking { it.setRecommendationRating(any(), any()) } doReturn Unit
    }
}
