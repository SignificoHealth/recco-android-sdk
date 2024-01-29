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
    private val interactionDelegate = mock<ContentInteractViewModelDelegate>()
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

//    @Test
//    fun `onFailure emits exceptions while logging them if ToggleDislikeState`() = runTest {
//        // When
//        repository.stubForToggleRatingFailure()
//        val events = onViewModelInteraction(3, ArticleUserInteract.ToggleDislikeState)
//
//        // Then
//        events.fastForEach {
//            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
//        }
//
//        verifyBlocking(logger, times(1)) {
//            e(staticThrowableForTesting, null, null)
//        }
//    }

//    @Test
//    fun `onFailure emits exceptions while logging them if ToggleBookmarkState`() = runTest {
//        // When
//        repository.stubForToggleBookmarkFailure()
//        val events = onViewModelInteraction(3, ArticleUserInteract.ToggleBookmarkState)
//
//        // Then
//        events.fastForEach {
//            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
//        }
//
//        verifyBlocking(logger, times(1)) {
//            e(staticThrowableForTesting, null, null)
//        }
//    }

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

    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int,
        vararg userInteractions: ArticleUserInteract
    ) = ArticleViewModel(savedStateHandle, repository, logger, interactionDelegate).run {
        onViewModelInteraction(
            viewState = viewState,
            eventsToDrop = eventsToDrop,
            runInteractions = { userInteractions.forEach { onUserInteract(it) } }
        )
    }
}
