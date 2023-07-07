package com.recco.internal.feature.bookmark

import androidx.compose.ui.util.fastForEach
import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.logger.Logger
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
class BookmarkViewModelTest {

    private lateinit var repository: RecommendationRepository
    private val logger = mock<Logger> {}

    @BeforeEach
    fun setup() {
        repository = mock()
    }

    @Test
    fun `onFailure emits exceptions while logging them if init fails and Retry`() = runTest {
        // When
        repository.stubRepositoryForError()
        val events = onViewModelInteraction(1, BookmarkUserInteract.Retry)

        // Then
        assertThat(events).isNotEmpty()
        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }

        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if init fails and Refresh`() = runTest {
        // When
        repository.stubRepositoryForError()
        val events = onViewModelInteraction(1, BookmarkUserInteract.Refresh)

        // Then

        assertThat(events).isNotEmpty()
        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `viewState initial state matches Loading`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        val events = onViewModelInteraction(0, BookmarkUserInteract.Retry)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiStateWithLoading)
    }

    @Test
    fun `viewState carries data when bookmarks pipeline emits correctly`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        val events = onViewModelInteraction(1, BookmarkUserInteract.Retry)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiStateWithData(bookmarkUI))
    }

    @Test
    fun `onRefresh updates viewState as expected and repository is triggered`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        val events = onViewModelInteraction(1, BookmarkUserInteract.Refresh)

        // Then
        verifyBlocking(repository) {
            reloadBookmarks()
        }

        assertThat(events.first()).isEqualTo(expectedUiStateWithData(bookmarkUI))
    }

    @Test
    fun `onRetry updates viewState as expected`() = runTest {
        // When
        repository.stubRepositoryForSuccess()
        val events = onViewModelInteraction(1, BookmarkUserInteract.Retry)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiStateWithData(bookmarkUI))
    }

    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int,
        vararg userInteractions: BookmarkUserInteract
    ) = BookmarkViewModel(repository, logger).run {
        onViewModelInteraction(
            viewState = viewState,
            eventsToDrop = eventsToDrop,
            runInteractions = { userInteractions.forEach { onUserInteract(it) } })
    }
}
