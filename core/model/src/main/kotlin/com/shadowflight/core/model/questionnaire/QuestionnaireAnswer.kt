package com.shadowflight.core.model.questionnaire

data class QuestionnaireAnswer(
    val questionId: String,
    val type: QuestionType,
    val multiChoice: List<Int>? = null,
    val numeric: Double? = null
)