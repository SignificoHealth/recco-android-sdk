package com.recco.internal.feature.article

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.SavedStateHandle
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.CoroutineTestExtension
import com.recco.internal.core.test.utils.expectedUiStateWithData
import com.recco.internal.core.test.utils.expectedUiStateWithError
import com.recco.internal.core.test.utils.expectedUiStateWithLoading
import com.recco.internal.core.test.utils.staticThrowableForTesting
import com.recco.internal.core.ui.components.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
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
    private lateinit var events: MutableList<UiState<ArticleUI>>

    private val logger = mock<Logger>()
    private val savedStateHandle = mock<SavedStateHandle>()

    @BeforeEach
    fun setup() {
        repository = mock()
        events = mutableListOf()
        savedStateHandle.stub()
    }

    @Test
    fun `onFailure emits exceptions while logging during initial load`() = runTest {
        // When
        repository.stubForInitialFailure()
        onViewModelInteraction(1, ArticleUserInteract.Retry)

        // Then
        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }

        assert(events.first() == expectedUiStateWithError)
    }

    @Test
    fun `onFailure emits exceptions while logging them if Retry`() = runTest {
        // When
        repository.stubForInitialFailure()
        onViewModelInteraction(3, ArticleUserInteract.Retry)

        // Then
        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }

        events.fastForEach { assert(it == expectedUiStateWithError) }
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleLikeState`() = runTest {
        // When
        repository.stubForToggleRatingFailure()
        onViewModelInteraction(3, ArticleUserInteract.ToggleLikeState)

        // Then
        events.fastForEach { assert(it == expectedUiStateWithError) }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleDislikeState`() = runTest {
        // When
        repository.stubForToggleRatingFailure()
        onViewModelInteraction(3, ArticleUserInteract.ToggleDislikeState)

        // Then
        events.fastForEach { assert(it == expectedUiStateWithError) }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleBookmarkState`() = runTest {
        // When
        repository.stubForToggleBookmarkFailure()
        onViewModelInteraction(3, ArticleUserInteract.ToggleBookmarkState)

        // Then
        events.fastForEach { assert(it == expectedUiStateWithError) }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `initial state event emitted is Loading`() = runTest {
        // When
        repository.stubForSuccessWithLikedAndBookmarkedArticle()
        onViewModelInteraction(0, ArticleUserInteract.Retry)

        // Then
        assert(events.first() == expectedUiStateWithLoading)
    }

    @Test
    fun `new article is provided if Retry`() = runTest {
        // Given
        val expectedArticle =
            createArticleUiGivenContent(rating = Rating.LIKE)
        val expectedUiState = expectedUiStateWithData(expectedArticle)

        // When
        repository.stubForSuccessWithLikedAndBookmarkedArticle()
        onViewModelInteraction(1, ArticleUserInteract.Retry)

        // Then
        assert(events.first() == expectedUiState)
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
        onViewModelInteraction(2, ArticleUserInteract.ToggleBookmarkState)

        // Then
        assert(events.first() == expectedUiState)
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
        onViewModelInteraction(2, ArticleUserInteract.ToggleBookmarkState)

        // Then
        assert(events.first() == expectedUiState)
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
        onViewModelInteraction(2, ArticleUserInteract.ToggleLikeState)

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
        onViewModelInteraction(2, ArticleUserInteract.ToggleDislikeState)

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
        onViewModelInteraction(2, ArticleUserInteract.ToggleDislikeState)

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
        onViewModelInteraction(2, ArticleUserInteract.ToggleLikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assert(uiState == dislikedUiState)
                1 -> assert(uiState == expectedUiState)
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
        onViewModelInteraction(2, ArticleUserInteract.ToggleLikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assert(uiState == nonRatedUiState)
                1 -> assert(uiState == expectedUiState)
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
        onViewModelInteraction(2, ArticleUserInteract.ToggleDislikeState)

        // Then
        events.forEachIndexed { index, uiState ->
            when (index) {
                0 -> assert(uiState == nonRatedUiState)
                1 -> assert(uiState == expectedUiState)
            }
        }
    }

    /**
     * @param eventsToDrop avoids collecting Loading or initialSubscribe events when needed.
     * @param userInteraction defines userInteraction taking place.
     */
    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int,
        userInteraction: ArticleUserInteract
    ) {
        ArticleViewModel(savedStateHandle, repository, logger).also { sut ->
            sut.viewState
                .drop(eventsToDrop)
                .onEach(events::add)
                .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
                .invokeOnCompletion { cancel() }
            sut.onUserInteract(userInteraction)
        }
        runCurrent()
    }
}
