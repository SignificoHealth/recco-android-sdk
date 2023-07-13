package com.recco.internal.feature.questionnaire

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.repository.QuestionnaireRepository
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.pipelines.GlobalViewEvent
import com.recco.internal.core.ui.pipelines.globalViewEvents
import com.recco.internal.core.ui.pipelines.showErrorToast
import com.recco.internal.feature.questionnaire.QuestionnaireUserInteract.BackClicked
import com.recco.internal.feature.questionnaire.QuestionnaireUserInteract.ClickOnMultiChoiceAnswerOption
import com.recco.internal.feature.questionnaire.QuestionnaireUserInteract.NextClicked
import com.recco.internal.feature.questionnaire.QuestionnaireUserInteract.Retry
import com.recco.internal.feature.questionnaire.QuestionnaireUserInteract.WriteOnNumericQuestion
import com.recco.internal.feature.questionnaire.navigation.feedSectionTypeArg
import com.recco.internal.feature.questionnaire.navigation.topicArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QuestionnaireViewModel @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val topic by lazy { savedStateHandle.get<Topic>(topicArg) }
    private val feedSectionType by lazy { savedStateHandle.get<FeedSectionType>(feedSectionTypeArg) }
    private val isOnboarding by lazy { topic == null }

    private val _viewState = MutableStateFlow(UiState<QuestionnaireUI>())
    val viewState: Flow<UiState<QuestionnaireUI>> = _viewState

    private val _viewEvents = MutableSharedFlow<QuestionnaireViewEvent>()
    val viewEvents: Flow<QuestionnaireViewEvent> = _viewEvents

    init {
        initialLoadSubscribe()
    }

    fun onUserInteract(userInteract: QuestionnaireUserInteract) {
        when (userInteract) {
            Retry -> initialLoadSubscribe()
            is ClickOnMultiChoiceAnswerOption -> clickOnMultiChoiceAnswerOption(userInteract)
            is WriteOnNumericQuestion -> writeOnNumericQuestion(userInteract)
            BackClicked -> viewModelScope.launch {
                scrollTo(Adjustment.BACKWARD)
            }

            NextClicked -> viewModelScope.launch {
                val uiState = _viewState.value
                val questionnaireUI = uiState.data ?: return@launch

                if (questionnaireUI.currentPage == questionnaireUI.questions.size - 1) {
                    submitQuestionnaire()
                } else {
                    scrollTo(Adjustment.FORWARD)
                }
            }
        }
    }

    private suspend fun scrollTo(adjustment: Adjustment) {
        val uiState = _viewState.value
        val questionnaireUI = uiState.data ?: return

        val newPage = questionnaireUI.currentPage + adjustment.value
        if (newPage < 0) return

        _viewEvents.emit(QuestionnaireViewEvent.ScrollTo(newPage))

        val questions = questionnaireUI.questions
        _viewState.emit(
            uiState.copy(
                data = questionnaireUI.copy(
                    progress = (newPage.toFloat() + 1) / questions.size.toFloat(),
                    currentPage = newPage,
                    showBack = newPage != 0,
                    isNextEnabled = questions[newPage].isAnswerInputValid(
                        requiredToBeAnswered = isOnboarding
                    ),
                    isLastPage = newPage == questions.size - 1,
                    isFirstPage = newPage == 0
                )
            )
        )
    }

    private fun initialLoadSubscribe() {
        viewModelScope.launch {
            runCatching {
                if (isOnboarding) {
                    questionnaireRepository.getOnboarding()
                } else {
                    questionnaireRepository.getQuestionnaireByTopic(topic!!)
                }
            }.onSuccess { questions ->
                _viewState.emit(
                    UiState(
                        isLoading = false,
                        data = QuestionnaireUI(
                            isNextEnabled = !isOnboarding,
                            questions = questions,
                            progress = 1f / questions.size
                        )
                    )
                )
            }.onFailure {
                _viewState.emit(UiState(error = it, isLoading = false))
                logger.e(it)
            }
        }
    }

    private fun clickOnMultiChoiceAnswerOption(input: ClickOnMultiChoiceAnswerOption) {
        viewModelScope.launch {
            val uiState = _viewState.value
            val questionnaireUI = _viewState.value.data ?: return@launch
            val (question, selectedOption) = input

            val updatedQuestion = question.copy(
                options = question.options.map {
                    when {
                        it.id == selectedOption.id -> {
                            if (question.isSingleChoice) {
                                it.copy(isSelected = true)
                            } else {
                                it.copy(isSelected = !selectedOption.isSelected)
                            }
                        }

                        question.isSingleChoice -> it.copy(isSelected = false)
                        else -> it
                    }
                }
            )

            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        questions = questionnaireUI.questions.map {
                            if (question.id == it.id) {
                                updatedQuestion
                            } else {
                                it
                            }
                        },
                        isNextEnabled = updatedQuestion.isAnswerInputValid(
                            requiredToBeAnswered = isOnboarding
                        )
                    )
                )
            )

            if (question.isSingleChoice && !questionnaireUI.isLastPage) {
                delay(timeMillis = 300)
                onUserInteract(NextClicked)
            }
        }
    }

    private fun writeOnNumericQuestion(input: WriteOnNumericQuestion) {
        viewModelScope.launch {
            val uiState = _viewState.value
            val questionnaireUI = _viewState.value.data ?: return@launch
            val (question, selectedValue) = input
            val updatedQuestion = question.copy(selectedValue = selectedValue.toDoubleOrNull())

            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        questions = questionnaireUI.questions.map {
                            if (question.id == it.id) {
                                updatedQuestion
                            } else {
                                it
                            }
                        },
                        isNextEnabled = updatedQuestion
                            .isAnswerInputValid(requiredToBeAnswered = isOnboarding)
                    )
                )
            )
        }
    }

    private suspend fun submitQuestionnaire() {
        val uiState = _viewState.value
        val questionnaireUI = _viewState.value.data ?: return

        _viewState.emit(
            uiState.copy(
                data = questionnaireUI.copy(
                    isQuestionnaireSubmitLoading = true
                )
            )
        )
        runCatching {
            if (isOnboarding) {
                questionnaireRepository.onboardingAnswers(questionnaireUI.questions)
            } else {
                questionnaireRepository.answers(questionnaireUI.questions)
            }
        }.onSuccess {
            if (!isOnboarding && feedSectionType != null) {
                globalViewEvents.emit(
                    GlobalViewEvent.FeedSectionToUnlock(
                        topic!!,
                        feedSectionType!!,
                        state = if (questionnaireUI.questions
                                .all { it.isAnswerInputValid(requiredToBeAnswered = true) }
                        ) {
                            FeedSectionState.UNLOCKED
                        } else {
                            FeedSectionState.PARTIALLY_UNLOCKED
                        }
                    )
                )
                _viewEvents.emit(QuestionnaireViewEvent.QuestionnaireSubmitted)
            } else {
                _viewEvents.emit(QuestionnaireViewEvent.QuestionnaireOnboardingSubmitted)
            }

            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        isQuestionnaireSubmitLoading = false
                    )
                )
            )
        }.onFailure { e ->
            logger.e(e)
            globalViewEvents.emit(showErrorToast(e))
            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        isQuestionnaireSubmitLoading = false
                    )
                )
            )
        }
    }
}

internal enum class Adjustment(val value: Int) {
    FORWARD(+1),
    BACKWARD(-1)
}