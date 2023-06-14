package com.shadowflight.core.questionnaire

import com.shadowflight.core.model.questionnaire.Question

data class QuestionnaireUI(
    val questions: List<Question>,
    val progress: Float = 0f,
    val currentPage: Int = 0,
    val totalPages: Int = questions.size,
    val showBack: Boolean = false,
    val isNextEnabled: Boolean = false,
    val isFirstPage: Boolean = true,
    val isLastPage: Boolean = false,
    val isQuestionnaireSubmitLoading: Boolean = false
)