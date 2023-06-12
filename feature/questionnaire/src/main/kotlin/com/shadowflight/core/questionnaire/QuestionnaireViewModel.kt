package com.shadowflight.core.questionnaire

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.questionnaire.QuestionnaireUserInteract.BackClicked
import com.shadowflight.core.questionnaire.QuestionnaireUserInteract.ClickOnMultiChoiceAnswerOption
import com.shadowflight.core.questionnaire.QuestionnaireUserInteract.NextClicked
import com.shadowflight.core.questionnaire.QuestionnaireUserInteract.Retry
import com.shadowflight.core.questionnaire.QuestionnaireUserInteract.WriteOnNumericQuestion
import com.shadowflight.core.questionnaire.navigation.topicArg
import com.shadowflight.core.repository.FeedRepository
import com.shadowflight.core.repository.QuestionnaireRepository
import com.shadowflight.core.repository.RecommendationRepository
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.components.UiState
import com.shadowflight.core.ui.pipelines.GlobalViewEvent
import com.shadowflight.core.ui.pipelines.ToastMessageType
import com.shadowflight.core.ui.pipelines.globalViewEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository,
    private val recommendationRepository: RecommendationRepository,
    private val feedRepository: FeedRepository,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val topic by lazy { checkNotNull(savedStateHandle.get<Topic>(topicArg)) }
    private val requiredToBeAnswered by lazy { false }

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
            BackClicked -> scrollTo(Adjustment.BACKWARD)
            NextClicked -> {
                val uiState = _viewState.value
                val questionnaireUI = uiState.data ?: return

                if (questionnaireUI.currentPage == questionnaireUI.questionnaire.questions.size - 1) {
                    submitQuestionnaire()
                } else {
                    scrollTo(Adjustment.FORWARD)
                }
            }
        }
    }

    private fun scrollTo(adjustment: Adjustment) {
        val uiState = _viewState.value
        val questionnaireUI = uiState.data ?: return

        val newPage = questionnaireUI.currentPage + adjustment.value
        if (newPage < 0) return

        viewModelScope.launch {
            _viewEvents.emit(QuestionnaireViewEvent.ScrollTo(newPage))
        }

        viewModelScope.launch {
            val questions = questionnaireUI.questionnaire.questions
            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        progress = (newPage.toFloat() + 1) / questions.size.toFloat(),
                        currentPage = newPage,
                        showBack = newPage != 0,
                        isNextEnabled = questions[newPage].isAnswerInputValid(
                            requiredToBeAnswered
                        ),
                        isLastPage = newPage == questionnaireUI.questionnaire.questions.size - 1,
                        isFirstPage = newPage == 0
                    )
                )
            )
        }
    }

    private fun initialLoadSubscribe() {
        viewModelScope.launch {
            runCatching { questionnaireRepository.getQuestionnaireByTopic(topic) }
                .onSuccess { questionnaire ->
                    _viewState.emit(
                        UiState(
                            isLoading = false,
                            data = QuestionnaireUI(
                                isNextEnabled = !requiredToBeAnswered,
                                questionnaire = questionnaire,
                                progress = 1f / questionnaire.questions.size
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
        val uiState = _viewState.value
        val questionnaireUI = _viewState.value.data ?: return
        val questionnaire = questionnaireUI.questionnaire
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

        viewModelScope.launch {
            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        questionnaire = questionnaire.copy(
                            questions = questionnaire.questions.map {
                                if (question.id == it.id) {
                                    updatedQuestion
                                } else {
                                    it
                                }
                            }
                        ),
                        isNextEnabled = updatedQuestion.isAnswerInputValid(requiredToBeAnswered)
                    )
                )
            )
        }

        if (question.isSingleChoice && !questionnaireUI.isLastPage) {
            viewModelScope.launch {
                delay(timeMillis = 300)
                onUserInteract(NextClicked)
            }
        }
    }

    private fun writeOnNumericQuestion(input: WriteOnNumericQuestion) {
        val uiState = _viewState.value
        val questionnaireUI = _viewState.value.data ?: return
        val questionnaire = questionnaireUI.questionnaire
        val (question, selectedValue) = input
        val updatedQuestion = question.copy(selectedValue = selectedValue.toDoubleOrNull())

        viewModelScope.launch {
            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        questionnaire = questionnaire.copy(
                            questions = questionnaire.questions.map {
                                if (question.id == it.id) {
                                    updatedQuestion
                                } else {
                                    it
                                }
                            }
                        ),
                        isNextEnabled = updatedQuestion.isAnswerInputValid(requiredToBeAnswered)
                    )
                )
            )
        }
    }

    private fun submitQuestionnaire() {
        val uiState = _viewState.value
        val questionnaireUI = _viewState.value.data ?: return

        viewModelScope.launch {
            _viewState.emit(
                uiState.copy(
                    data = questionnaireUI.copy(
                        isQuestionnaireSubmitLoading = true
                    )
                )
            )
            runCatching {
                questionnaireRepository.answers(questionnaireUI.questionnaire)
            }.onSuccess {
                globalViewEvents.emit(GlobalViewEvent.ResetFeedScroll)
                feedRepository.reloadFeed()
                recommendationRepository.reloadSection(topic)
                _viewEvents.emit(QuestionnaireViewEvent.QuestionnaireSubmitted)
                _viewState.emit(
                    uiState.copy(
                        data = questionnaireUI.copy(
                            isQuestionnaireSubmitLoading = false
                        )
                    )
                )
            }.onFailure { e ->
                logger.e(e)
                globalViewEvents.emit(
                    GlobalViewEvent.ShowToast(
                        titleRes = R.string.common_error_desc,
                        type = ToastMessageType.Error
                    )
                )
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
}

enum class Adjustment(val value: Int) {
    FORWARD(+1),
    BACKWARD(-1)
}