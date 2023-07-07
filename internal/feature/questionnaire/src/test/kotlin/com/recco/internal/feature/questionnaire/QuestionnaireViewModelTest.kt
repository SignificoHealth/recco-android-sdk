package com.recco.internal.feature.questionnaire

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.SavedStateHandle
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.questionnaire.MultiChoiceQuestion
import com.recco.internal.core.model.questionnaire.NumericQuestion
import com.recco.internal.core.repository.QuestionnaireRepository
import com.recco.internal.core.test.CoroutineTestExtension
import com.recco.internal.core.test.utils.expectedUiStateWithError
import com.recco.internal.core.test.utils.expectedUiStateWithLoading
import com.recco.internal.core.test.utils.staticThrowableForTesting
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.feature.questionnaire.QuestionnaireUserInteract.*
import com.recco.internal.feature.questionnaire.navigation.feedSectionTypeArg
import com.recco.internal.feature.questionnaire.navigation.topicArg
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
class QuestionnaireViewModelTest {
    private lateinit var repository: QuestionnaireRepository
    private lateinit var events: MutableList<UiState<QuestionnaireUI>>

    private val logger = mock<Logger>()
    private val savedStateHandle = mock<SavedStateHandle>()

    @BeforeEach
    fun setup() {
        repository = mock()
        events = mutableListOf()
        savedStateHandle.stub()
    }

    @Test
    fun `initial state event emitted is Loading`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)
        onViewModelInteraction(eventsToDrop = 0)

        // Then
        assert(events.first() == expectedUiStateWithLoading)
    }

    @Test
    fun `onFailure emits exceptions on Initial Load`() = runTest {
        // When
        repository.stubForInitialFailure()
        onViewModelInteraction(eventsToDrop = 1)

        // Then
        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }

        events.fastForEach { assert(it == expectedUiStateWithError) }
    }

    @Test
    fun `onFailure emits exceptions while logging them if Retry`() = runTest {
        // When
        repository.stubForInitialFailure()
        onViewModelInteraction(eventsToDrop = 3, Retry)

        // Then
        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }

        events.fastForEach { assert(it == expectedUiStateWithError) }
    }

    @Test
    fun `state is updated according to ClickOnMultiChoiceAnswerOption`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)

        onViewModelInteraction(
            eventsToDrop = 2,
            ClickOnMultiChoiceAnswerOption(
                question = multiChoiceQuestion,
                option = multiChoiceQuestion.options.first()
            )
        )

        // Then
        events.first().data!!.apply {
            assert(progress == .5f)
            assert(isNextEnabled)
            assert(!isLastPage)
            assert((questions.first() as MultiChoiceQuestion).options.first().isSelected)
        }
    }

    @Test
    fun `state is updated according to WriteOnNumericQuestion`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)

        onViewModelInteraction(
            eventsToDrop = 2,
            WriteOnNumericQuestion(
                question = numericQuestion,
                value = "345"
            )
        )

        // Then
        events.first().data!!.apply {
            assert(progress == .5f)
            assert(!isNextEnabled)
            assert((questions[1] as NumericQuestion).selectedValue == 345.0)
        }
    }

    @Test
    fun `state is updated according to NextClicked`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)

        onViewModelInteraction(
            eventsToDrop = 2,
            NextClicked
        )

        // Then
        events.first().data!!.apply {
            assert(progress == 1f)
            assert(isNextEnabled)
            assert(isLastPage)
        }
    }


    @Test
    fun `state is updated according to NextClicked on lastPage`() = runTest {
        // When
        repository.apply {
            stubForInitialSuccess(questions)
            stubForSendAnswersSuccess()
        }

        onViewModelInteraction(
            eventsToDrop = 5,
            ClickOnMultiChoiceAnswerOption(
                question = multiChoiceQuestion,
                option = multiChoiceQuestion.options.first()
            ),
            NextClicked,
            WriteOnNumericQuestion(
                question = numericQuestion,
                value = "345"
            ),
            NextClicked
        )

        verifyBlocking(logger, times(0)) {
            e(staticThrowableForTesting, null, null)
        }

        assert(events[0].data!!.isQuestionnaireSubmitLoading)
        assert(!events[1].data!!.isQuestionnaireSubmitLoading)
    }

    @Test
    fun `onFailure emits exceptions on NextClicked on lastPage`() = runTest {
        // When
        repository.apply {
            stubForInitialSuccess(questions)
            stubForSendAnswersFailure()
        }

        onViewModelInteraction(
            eventsToDrop = 5,
            ClickOnMultiChoiceAnswerOption(
                question = multiChoiceQuestion,
                option = multiChoiceQuestion.options.first()
            ),
            NextClicked,
            WriteOnNumericQuestion(
                question = numericQuestion,
                value = "345"
            ),
            NextClicked
        )

        // Then
        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }

        assert(events[0].data!!.isQuestionnaireSubmitLoading)
        assert(!events[1].data!!.isQuestionnaireSubmitLoading)
    }

    @Test
    fun `state is updated according to BackClicked`() = runTest {
        // When
        repository.apply {
            stubForInitialSuccess(questions)
            stubForSendAnswersSuccess()
        }

        onViewModelInteraction(
            eventsToDrop = 4,
            ClickOnMultiChoiceAnswerOption(
                question = multiChoiceQuestion,
                option = multiChoiceQuestion.options.first()
            ),
            NextClicked,
            BackClicked
        )

        // Then
        events.first().data!!.apply {
            assert(progress == .5f)
            assert(!showBack)
            assert(isFirstPage)
        }
    }

    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int = 0,
        vararg userInteractions: QuestionnaireUserInteract
    ) {
        QuestionnaireViewModel(repository, logger, savedStateHandle).also { sut ->
            sut.viewState
                .drop(eventsToDrop)
                .onEach(events::add)
                .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
                .invokeOnCompletion { cancel() }
            userInteractions.forEach { sut.onUserInteract(it) }
        }
        runCurrent()
    }

}