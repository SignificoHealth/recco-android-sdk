package com.recco.internal.feature.bookmark

import androidx.compose.ui.util.fastForEach
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.CoroutineTestExtension
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.feature.bookmark.model.expectedWithData
import com.recco.internal.feature.bookmark.model.expectedWithError
import com.recco.internal.feature.bookmark.model.expectedWithLoading
import com.recco.internal.feature.bookmark.model.staticThrowableForTesting
import com.recco.internal.feature.bookmark.utils.stubRepositoryForError
import com.recco.internal.feature.bookmark.utils.stubRepositoryForSuccess
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
class BookmarkViewModelTest {

    private lateinit var repository: RecommendationRepository
    private lateinit var events: MutableList<UiState<BookmarkUI>>

    private val logger = mock<Logger> {}

    @BeforeEach
    fun setup() {
        repository = mock()
        events = mutableListOf()
    }

    @Test
    fun `onFailure emits exceptions while logging them if init fails and Retry`() = runTest {
        // When
        repository.stubRepositoryForError()
        onViewModelInteraction(1, BookmarkUserInteract.Retry)

        // Then
        assert(events.isNotEmpty())
        events.fastForEach { assert(it == expectedWithError) }

        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if init fails and Refresh`() = runTest {
        // When
        repository.stubRepositoryForError()
        onViewModelInteraction(1, BookmarkUserInteract.Refresh)

        // Then
        assert(events.isNotEmpty())
        events.fastForEach { assert(it == expectedWithError) }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `viewState initial state matches Loading`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        onViewModelInteraction(0, BookmarkUserInteract.Retry)

        // Then
        assert(events.first() == expectedWithLoading)
    }

    @Test
    fun `viewState carries data when bookmarks pipeline emits correctly`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        onViewModelInteraction(1, BookmarkUserInteract.Retry)

        // Then
        assert(events.first() == expectedWithData)
    }

    @Test
    fun `onRefresh updates viewState as expected and repository is triggered`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        onViewModelInteraction(1, BookmarkUserInteract.Refresh)

        // Then
        verifyBlocking(repository) {
            reloadBookmarks()
        }

        assert(events.first() == expectedWithData)
    }

    @Test
    fun `onRetry updates viewState as expected`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        onViewModelInteraction(1, BookmarkUserInteract.Retry)

        // Then
        assert(events.first() == expectedWithData)
    }

    /**
     * @param eventsToDrop avoids collecting Loading or initialSubscribe events when needed.
     * @param userInteraction defines userInteraction taking place.
     */
    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int,
        userInteraction: BookmarkUserInteract
    ) {

        BookmarkViewModel(repository, logger).also { sut ->
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
