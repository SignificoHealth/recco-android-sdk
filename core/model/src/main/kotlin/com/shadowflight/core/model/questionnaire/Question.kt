package com.shadowflight.core.model.questionnaire

data class Question(
    val id: String,
    val index: Int,
    val text: String,
    val type: QuestionType,
    val multiChoice: MultiChoiceQuestion? = null,
    val numeric: NumericQuestion? = null
)