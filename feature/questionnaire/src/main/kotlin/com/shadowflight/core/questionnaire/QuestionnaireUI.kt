package com.shadowflight.core.questionnaire

import com.shadowflight.core.model.questionnaire.Questionnaire

data class QuestionnaireUI(
    val questionnaire: Questionnaire,
    val progress: Float = (100 / questionnaire.questions.size) / 100.0f,
    val currentPage: Int = 0,
    val totalPages: Int = questionnaire.questions.size,
    val showBack: Boolean = false,
    val isNextEnabled: Boolean = false,
    val isFirstPage: Boolean = true,
    val isLastPage: Boolean = false,
    val isQuestionnaireSubmitLoading: Boolean = false
)