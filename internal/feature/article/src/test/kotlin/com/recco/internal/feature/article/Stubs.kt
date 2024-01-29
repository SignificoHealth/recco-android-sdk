package com.recco.internal.feature.article

import androidx.lifecycle.SavedStateHandle
import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.utils.staticThrowableForTesting
import com.recco.internal.core.ui.preview.ContentIdPreviewProvider
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.stub

internal fun SavedStateHandle.stub() {
    stub {
        on { it.get<ContentId>(any()) } doReturn (ContentIdPreviewProvider.data)
    }
}

internal fun RecommendationRepository.stubForSuccessWithLikedAndBookmarkedArticle() {
    stubRepositoryForSuccess(createArticle(rating = Rating.LIKE))
}

internal fun RecommendationRepository.stubForSuccessWithDislikedArticle() {
    stubRepositoryForSuccess(createArticle(rating = Rating.DISLIKE))
}

internal fun RecommendationRepository.stubForSuccessWithNonRatedArticle() {
    stubRepositoryForSuccess(createArticle())
}

internal fun RecommendationRepository.stubForSuccessWithNonBookmarkedArticle() {
    stubRepositoryForSuccess(createArticle(isBookmarked = false))
}

internal fun RecommendationRepository.stubForInitialFailure() {
    stub {
        onBlocking { it.getArticle(any()) } doThrow staticThrowableForTesting
        onBlocking { it.setRecommendationAsViewed(any()) } doThrow staticThrowableForTesting
    }
}

internal fun RecommendationRepository.stubForToggleRatingFailure() {
    stub {
        onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
        onBlocking { it.getArticle(any()) } doReturn createArticle(rating = Rating.LIKE)
        onBlocking {
            it.setRecommendationRating(
                any(),
                any()
            )
        } doThrow staticThrowableForTesting
    }
}

internal fun RecommendationRepository.stubForToggleBookmarkFailure() {
    stub {
        onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
        onBlocking { it.getArticle(any()) } doReturn createArticle(rating = Rating.LIKE)
        onBlocking {
            it.setBookmarkRecommendation(
                any(),
                any()
            )
        } doThrow staticThrowableForTesting
    }
}

private fun RecommendationRepository.stubRepositoryForSuccess(article: Article) {
    stub {
        onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
        onBlocking { it.getArticle(any()) } doReturn article
        onBlocking { it.setBookmarkRecommendation(any(), any()) } doReturn Unit
        onBlocking { it.setRecommendationRating(any(), any()) } doReturn Unit
    }
}

private fun RecommendationRepository.stubRepositoryForSuccess(id: ContentId) {
    stub {
        onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
        onBlocking { it.setBookmarkRecommendation(any(), any()) } doReturn Unit
        onBlocking { it.setRecommendationRating(any(), any()) } doReturn Unit
    }
}
