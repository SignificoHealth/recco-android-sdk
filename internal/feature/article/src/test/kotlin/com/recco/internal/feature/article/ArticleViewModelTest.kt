package com.recco.internal.feature.article

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.CoroutineTestExtension
import com.recco.internal.core.test.extensions.onViewModelInteraction
import com.recco.internal.core.test.utils.expectedUiStateWithData
import com.recco.internal.core.test.utils.expectedUiStateWithError
import com.recco.internal.core.test.utils.expectedUiStateWithLoading
import com.recco.internal.core.test.utils.staticThrowableForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
class ArticleViewModelTest {
    private lateinit var repository: RecommendationRepository
    private val logger = mock<Logger>()
    private val savedStateHandle = mock<SavedStateHandle>().apply { stub() }

    @BeforeEach
    fun setup() {
        repository = mock()
    }

    @Test
    fun `onFailure emits exceptions while logging during initial load`() = runTest {
        // When
        repository.stubForInitialFailure()
        val events = onViewModelInteraction(1, ArticleUserInteract.Retry)

        // Then
        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }

        assertThat(events.first()).isEqualTo(expectedUiStateWithError)
    }

    @Test
    fun `onFailure emits exceptions while logging them if Retry`() = runTest {
        // When
        repository.stubForInitialFailure()
        val events = onViewModelInteraction(3, ArticleUserInteract.Retry)

        // Then
        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }

        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleLikeState`() = runTest {
        // When
        repository.stubForToggleRatingFailure()
        val events = onViewModelInteraction(3, ArticleUserInteract.ToggleLikeState)

        // Then
        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleDislikeState`() = runTest {
        // When
        repository.stubForToggleRatingFailure()
        val events = onViewModelInteraction(3, ArticleUserInteract.ToggleDislikeState)

        // Then
        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleBookmarkState`() = runTest {
        // When
        repository.stubForToggleBookmarkFailure()
        val events = onViewModelInteraction(3, ArticleUserInteract.ToggleBookmarkState)

        // Then
        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `initial state event emitted is Loading`() = runTest {
        // When
        repository.stubForSuccessWithLikedAndBookmarkedArticle()
        val events = onViewModelInteraction(0, ArticleUserInteract.Retry)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiStateWithLoading)
    }

    @Test
    fun `new article is provided if Retry`() = runTest {
        // Given
        val expectedArticle =
            createArticleUiGivenContent(rating = Rating.LIKE)
        val expectedUiState = expectedUiStateWithData(expectedArticle)

        // When
        repository.stubForSuccessWithLikedAndBookmarkedArticle()
        val events = onViewModelInteraction(1, ArticleUserInteract.Retry)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiState)
    }

    @Test
    fun `bookmarked article is updated according to the opposite logic provided`() = runTest {
        // Given
        val expectedArticle =
            createArticleUiGivenContent(rating = Rating.LIKE)
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isBookmarkLoading = false,
                    isBookmarked = false
                )
            )
        )

        // When
        repository.stubForSuccessWithLikedAndBookmarkedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleBookmarkState)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiState)
    }

    @Test
    fun `non bookmarked article is updated according to the opposite logic provided`() = runTest {
        // Given
        val expectedArticle =
            createArticleUiGivenContent(isBookmarked = false)
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    rating = Rating.NOT_RATED,
                    isBookmarkLoading = false,
                    isBookmarked = true
                )
            )
        )

        // When
        repository.stubForSuccessWithNonBookmarkedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleBookmarkState)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiState)
    }

    @Test
    fun `liked article turns into non rated if liked again`() = runTest {
        // Given
        val expectedArticle =
            createArticleUiGivenContent(rating = Rating.LIKE)
        val likedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = true,
                    rating = Rating.LIKE
                )
            )
        )
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.NOT_RATED
                )
            )
        )

        // When
        repository.stubForSuccessWithLikedAndBookmarkedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleLikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assert(uiState == likedUiState)
                1 -> assert(uiState == expectedUiState)
            }
        }
    }

    @Test
    fun `disliked article turns into non rated if disliked again`() = runTest {
        // Given
        val expectedArticle =
            createArticleUiGivenContent(rating = Rating.DISLIKE)
        val dislikedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = true,
                    isLikeLoading = false,
                    rating = Rating.DISLIKE
                )
            )
        )
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.NOT_RATED
                )
            )
        )

        // When
        repository.stubForSuccessWithDislikedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleDislikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assert(uiState == dislikedUiState)
                1 -> assert(uiState == expectedUiState)
            }
        }
    }

    @Test
    fun `liked article turns into disliked if disliked`() = runTest {
        // Given
        val expectedArticle =
            createArticleUiGivenContent(rating = Rating.LIKE)
        val likedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = true,
                    isLikeLoading = false,
                    rating = Rating.LIKE
                )
            )
        )
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.DISLIKE
                )
            )
        )

        // When
        repository.stubForSuccessWithLikedAndBookmarkedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleDislikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assert(uiState == likedUiState)
                1 -> assert(uiState == expectedUiState)
            }
        }
    }

    @Test
    fun `disliked article turns into liked if liked`() = runTest {
        // Given
        val expectedArticle = createArticleUiGivenContent(rating = Rating.DISLIKE)
        val dislikedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = true,
                    rating = Rating.DISLIKE
                )
            )
        )
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.LIKE
                )
            )
        )

        // When
        repository.stubForSuccessWithDislikedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleLikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assertThat(uiState).isEqualTo(dislikedUiState)
                1 -> assertThat(uiState).isEqualTo(expectedUiState)
            }
        }
    }

    @Test
    fun `nonrated article turns into liked if liked`() = runTest {
        // Given
        val expectedArticle = createArticleUiGivenContent()
        val nonRatedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = true,
                    rating = Rating.NOT_RATED
                )
            )
        )
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.LIKE
                )
            )
        )

        // When
        repository.stubForSuccessWithNonRatedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleLikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assertThat(uiState).isEqualTo(nonRatedUiState)
                1 -> assertThat(uiState).isEqualTo(expectedUiState)
            }
        }
    }

    @Test
    fun `non rated article turns into disliked if disliked`() = runTest {
        // Given
        val expectedArticle = createArticleUiGivenContent()
        val nonRatedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = true,
                    isLikeLoading = false,
                    rating = Rating.NOT_RATED
                )
            )
        )
        val expectedUiState = expectedUiStateWithData(expectedArticle).copy(
            data = expectedArticle.copy(
                userInteraction = genericUserInteraction.copy(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.DISLIKE
                )
            )
        )

        // When
        repository.stubForSuccessWithNonRatedArticle()
        val events = onViewModelInteraction(2, ArticleUserInteract.ToggleDislikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assertThat(uiState).isEqualTo(nonRatedUiState)
                1 -> assertThat(uiState).isEqualTo(expectedUiState)
            }
        }
    }

    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int,
        vararg userInteractions: ArticleUserInteract
    ) = ArticleViewModel(savedStateHandle, repository, logger).run {
        onViewModelInteraction(
            viewState = viewState,
            eventsToDrop = eventsToDrop,
            runInteractions = { userInteractions.forEach { onUserInteract(it) } }
        )
    }
}
