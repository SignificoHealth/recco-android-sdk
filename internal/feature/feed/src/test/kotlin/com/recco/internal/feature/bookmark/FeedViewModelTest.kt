package com.recco.internal.feature.bookmark

import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.repository.FeedRepository
import com.recco.internal.core.repository.MetricRepository
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.CoroutineTestExtension
import com.recco.internal.core.test.extensions.onViewModelInteraction
import com.recco.internal.core.test.utils.expectedUiStateWithError
import com.recco.internal.core.test.utils.expectedUiStateWithLoading
import com.recco.internal.core.test.utils.staticThrowableForTesting
import com.recco.internal.core.ui.pipelines.GlobalViewEvent
import com.recco.internal.core.ui.pipelines.globalViewEvents
import com.recco.internal.feature.feed.FeedUserInteract
import com.recco.internal.feature.feed.FeedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
class FeedViewModelTest {
    private lateinit var feedRepository: FeedRepository
    private lateinit var recRepository: RecommendationRepository
    private lateinit var metricRepository: MetricRepository

    private val logger = mock<Logger> {}

    @BeforeEach
    fun setup() {
        recRepository = mock()
        feedRepository = mock()
        metricRepository = mock()
    }

    @Test
    fun `initial state event emitted is Loading`() = runTest {
        // When
        stubReposForSuccess()
        val events = onViewModelInteraction(eventsToDrop = 0)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiStateWithLoading)
    }

    @Test
    fun `onFailure emits exceptions while logging them if init fails and Retry`() = runTest {
        // When
        stubReposForError()
        val events = onViewModelInteraction(1, FeedUserInteract.Retry)

        verifyBlocking(logger, times(3)) {
            e(any(), anyOrNull(), anyOrNull())
        }

        events.first().apply {
            assertThat(isLoading).isEqualTo(expectedUiStateWithError.isLoading)
            assertThat(data).isEqualTo(expectedUiStateWithError.data)
            assertThat(error).isNotNull()
        }
    }

    @Test
    fun `onFailure emits exceptions on Initial Load`() = runTest {
        // When
        stubReposForError()
        val events = onViewModelInteraction(eventsToDrop = 1)

        // Then
        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }

        events.first().apply {
            assertThat(isLoading).isEqualTo(expectedUiStateWithError.isLoading)
            assertThat(data).isEqualTo(expectedUiStateWithError.data)
            assertThat(error).isNotNull()
        }
    }

    @Test
    fun `viewState carries data when pipelines emit correctly`() = runTest {
        // When
        stubReposForSuccess()
        val events = onViewModelInteraction(1)

        // Then
        events.first().apply {
            assertThat(isLoading).isEqualTo(expectedUiStateWithError.isLoading)
            assertThat(data!!.sections.count()).isEqualTo(feedUI.sections.count())
            assertThat(error).isNull()
        }
    }

    @Test
    fun `refresh updates viewState as expected and repository is triggered`() = runTest {
        // When
        stubReposForSuccess()
        val events = onViewModelInteraction(1, FeedUserInteract.Refresh)

        // Then
        verifyBlocking(feedRepository) { reloadFeed() }
        verifyBlocking(recRepository) { reloadAllSections() }

        // Then
        events.first().apply {
            assertThat(isLoading).isEqualTo(expectedUiStateWithError.isLoading)
            assertThat(data!!.sections.count()).isEqualTo(feedUI.sections.count())
            assertThat(error).isNull()
        }
    }

    @Test
    fun `refresh unlocked section triggers expected calls to repository`() = runTest {
        // When
        stubReposForSuccess()
        val events = onViewModelInteraction(1, FeedUserInteract.RefreshUnlockedFeedSection)

        val globalEvent = GlobalViewEvent.FeedSectionToUnlock(
            topic = Topic.SLEEP,
            type = FeedSectionType.SLEEP_RECOMMENDATIONS,
            state = FeedSectionState.UNLOCKED
        )
        globalViewEvents.emit(globalEvent)

        // Then
        verifyBlocking(feedRepository) { setFeedSectionState(globalEvent.type, globalEvent.state) }
        verifyBlocking(recRepository) { reloadSection(globalEvent.topic) }

        // Then
        events.first().apply {
            assertThat(isLoading).isEqualTo(expectedUiStateWithError.isLoading)
            assertThat(data!!.sections.count()).isEqualTo(feedUI.sections.count())
            assertThat(error).isNull()
        }
    }

    private fun stubReposForSuccess() {
        recRepository.stubRepositoryForSuccess()
        feedRepository.stubRepositoryForSuccess()
    }

    private fun stubReposForError() {
        recRepository.stubRepositoryForError()
        feedRepository.stubRepositoryForError()
        metricRepository.stubRepositoryForError()
    }

    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int,
        vararg userInteractions: FeedUserInteract
    ) = FeedViewModel(feedRepository, recRepository, metricRepository, logger).run {
        onViewModelInteraction(
            viewState = viewState,
            eventsToDrop = eventsToDrop,
            runInteractions = { userInteractions.forEach { onUserInteract(it) } })
    }
}
