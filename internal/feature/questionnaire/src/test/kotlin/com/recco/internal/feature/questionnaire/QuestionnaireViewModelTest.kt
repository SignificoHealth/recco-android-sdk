package com.recco.internal.feature.questionnaire

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.questionnaire.MultiChoiceQuestion
import com.recco.internal.core.model.questionnaire.NumericQuestion
import com.recco.internal.core.repository.QuestionnaireRepository
import com.recco.internal.core.test.CoroutineTestExtension
import com.recco.internal.core.test.extensions.onViewModelInteraction
import com.recco.internal.core.test.utils.expectedUiStateWithError
import com.recco.internal.core.test.utils.expectedUiStateWithLoading
import com.recco.internal.core.test.utils.staticThrowableForTesting
import com.recco.internal.feature.questionnaire.QuestionnaireUserInteract.*
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
class QuestionnaireViewModelTest {
    private lateinit var repository: QuestionnaireRepository
    private val logger = mock<Logger>()
    private val savedStateHandle = mock<SavedStateHandle>().apply { stub() }

    @BeforeEach
    fun setup() {
        repository = mock()
    }

    @Test
    fun `initial state event emitted is Loading`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)
        val events = onViewModelInteraction(eventsToDrop = 0)

        // Then
        assertThat(events.first()).isEqualTo(expectedUiStateWithLoading)
    }

    @Test
    fun `onFailure emits exceptions on Initial Load`() = runTest {
        // When
        repository.stubForInitialFailure()
        val events = onViewModelInteraction(eventsToDrop = 1)

        // Then
        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }

        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if Retry`() = runTest {
        // When
        repository.stubForInitialFailure()
        val events = onViewModelInteraction(eventsToDrop = 3, Retry)

        // Then
        verifyBlocking(logger, times(2)) {
            e(staticThrowableForTesting, null, null)
        }

        events.fastForEach {
            assertThat(events.first()).isEqualTo(expectedUiStateWithError)
        }
    }

    @Test
    fun `state is updated according to ClickOnMultiChoiceAnswerOption`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)

        val events = onViewModelInteraction(
            eventsToDrop = 2,
            ClickOnMultiChoiceAnswerOption(
                question = multiChoiceQuestion,
                option = multiChoiceQuestion.options.first()
            )
        )

        // Then
        events.first().data!!.apply {
            assertThat(progress).isEqualTo(.5f)
            assertThat(isLastPage).isFalse()
            assertThat(isNextEnabled).isTrue()
            assertThat((questions.first() as MultiChoiceQuestion).options.first().isSelected).isTrue()
        }
    }

    @Test
    fun `state is updated according to WriteOnNumericQuestion`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)

        val events = onViewModelInteraction(
            eventsToDrop = 2,
            WriteOnNumericQuestion(
                question = numericQuestion,
                value = "345"
            )
        )

        // Then
        events.first().data!!.apply {
            assertThat(progress).isEqualTo(.5f)
            assertThat((questions[1] as NumericQuestion).selectedValue).isEqualTo(345.0)
            assertThat(isNextEnabled).isFalse()
        }
    }

    @Test
    fun `state is updated according to NextClicked`() = runTest {
        // When
        repository.stubForInitialSuccess(questions)

        val events = onViewModelInteraction(
            eventsToDrop = 2,
            NextClicked
        )

        // Then
        events.first().data!!.apply {
            assertThat(progress).isEqualTo(1f)
            assertThat(isLastPage).isTrue()
            assertThat(isNextEnabled).isTrue()
        }
    }


    @Test
    fun `state is updated according to NextClicked on lastPage`() = runTest {
        // When
        repository.apply {
            stubForInitialSuccess(questions)
            stubForSendAnswersSuccess()
        }

        val events = onViewModelInteraction(
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

        assertThat(events[0].data?.isQuestionnaireSubmitLoading).isTrue()
        assertThat(events[1].data?.isQuestionnaireSubmitLoading).isFalse()
    }

    @Test
    fun `onFailure emits exceptions on NextClicked on lastPage`() = runTest {
        // When
        repository.apply {
            stubForInitialSuccess(questions)
            stubForSendAnswersFailure()
        }

        val events = onViewModelInteraction(
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

        assertThat(events[0].data?.isQuestionnaireSubmitLoading).isTrue()
        assertThat(events[1].data?.isQuestionnaireSubmitLoading).isFalse()
    }

    @Test
    fun `state is updated according to BackClicked`() = runTest {
        // When
        repository.apply {
            stubForInitialSuccess(questions)
            stubForSendAnswersSuccess()
        }

        val events = onViewModelInteraction(
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
            assertThat(progress).isEqualTo(.5f)
            assertThat(showBack).isFalse()
            assertThat(isFirstPage).isTrue()
        }
    }

    private fun TestScope.onViewModelInteraction(
        eventsToDrop: Int,
        vararg userInteractions: QuestionnaireUserInteract
    ) = QuestionnaireViewModel(repository, logger, savedStateHandle).run {
        onViewModelInteraction(
            viewState = viewState,
            eventsToDrop = eventsToDrop,
            runInteractions = { userInteractions.forEach { onUserInteract(it) } })
    }
}