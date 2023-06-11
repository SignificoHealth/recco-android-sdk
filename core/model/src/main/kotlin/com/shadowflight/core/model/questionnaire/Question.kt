package com.shadowflight.core.model.questionnaire

sealed class Question {
    abstract val id: String
    abstract val index: Int
    abstract val text: String
    abstract fun isAnswerInputValid(requiredToBeAnswered: Boolean): Boolean
}

data class MultiChoiceQuestion(
    override val id: String,
    override val index: Int,
    override val text: String,
    val maxOptions: Int,
    val minOptions: Int,
    val isSingleChoice: Boolean = maxOptions == 1,
    val options: List<MultiChoiceAnswerOption>,
) : Question() {
    override fun isAnswerInputValid(requiredToBeAnswered: Boolean): Boolean =
        if (!requiredToBeAnswered && options.none { it.isSelected }) {
            true
        } else {
            options.count { it.isSelected } in minOptions until maxOptions + 1
        }
}

data class NumericQuestion(
    override val id: String,
    override val index: Int,
    override val text: String,
    val maxValue: Int,
    val minValue: Int,
    val selectedValue: Double?,
    val format: NumericQuestionFormat
) : Question() {
    override fun isAnswerInputValid(requiredToBeAnswered: Boolean): Boolean =
        if (!requiredToBeAnswered) {
            selectedValue == null
                    || (selectedValue >= minValue && selectedValue <= maxValue)
        } else {
            selectedValue != null
                    && selectedValue >= minValue
                    && selectedValue <= maxValue
        }
}

enum class NumericQuestionFormat {
    HUMAN_HEIGHT,
    HUMAN_WEIGHT,
    INTEGER,
    DECIMAL;
}