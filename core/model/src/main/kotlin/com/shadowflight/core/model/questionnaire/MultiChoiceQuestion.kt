package com.shadowflight.core.model.questionnaire

data class MultiChoiceQuestion(
    val maxOptions: Int,
    val minOptions: Int,
    val options: List<MultiChoiceAnswerOption>
)