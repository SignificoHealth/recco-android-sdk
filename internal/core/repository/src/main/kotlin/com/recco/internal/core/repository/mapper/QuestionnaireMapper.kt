package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.questionnaire.MultiChoiceAnswerOption
import com.recco.internal.core.model.questionnaire.MultiChoiceQuestion
import com.recco.internal.core.model.questionnaire.NumericQuestion
import com.recco.internal.core.model.questionnaire.NumericQuestionFormat
import com.recco.internal.core.model.questionnaire.Question
import com.recco.internal.core.openapi.model.CreateQuestionnaireAnswerDTO
import com.recco.internal.core.openapi.model.NumericQuestionDTO
import com.recco.internal.core.openapi.model.QuestionAnswerTypeDTO
import com.recco.internal.core.openapi.model.QuestionDTO

internal fun QuestionDTO.asEntity() = when (type) {
    QuestionAnswerTypeDTO.MULTICHOICE -> MultiChoiceQuestion(
        questionnaireId = questionnaireId,
        id = id,
        index = index,
        text = text,
        maxOptions = multiChoice!!.maxOptions,
        minOptions = multiChoice!!.minOptions,
        options = multiChoice!!.options.map { answerOption ->
            MultiChoiceAnswerOption(
                id = answerOption.id,
                text = answerOption.text,
                isSelected = multiChoiceSelectedIds.orEmpty().contains(answerOption.id)
            )
        }
    )

    QuestionAnswerTypeDTO.NUMERIC -> NumericQuestion(
        questionnaireId = questionnaireId,
        id = id,
        index = index,
        text = text,
        maxValue = numeric!!.maxValue,
        minValue = numeric!!.minValue,
        selectedValue = numericSelected,
        format = numeric!!.format.asEntity()
    )
}

private fun NumericQuestionDTO.Format.asEntity() = when (this) {
    NumericQuestionDTO.Format.HUMAN_HEIGHT -> NumericQuestionFormat.HUMAN_HEIGHT
    NumericQuestionDTO.Format.HUMAN_WEIGHT -> NumericQuestionFormat.HUMAN_WEIGHT
    NumericQuestionDTO.Format.INTEGER -> NumericQuestionFormat.INTEGER
    NumericQuestionDTO.Format.DECIMAL -> NumericQuestionFormat.DECIMAL
}

fun Question.asDTO() = CreateQuestionnaireAnswerDTO(
    questionnaireId = questionnaireId,
    questionId = id,
    type = when (this) {
        is MultiChoiceQuestion -> QuestionAnswerTypeDTO.MULTICHOICE
        is NumericQuestion -> QuestionAnswerTypeDTO.NUMERIC
    },
    multichoice = when (this) {
        is MultiChoiceQuestion -> options.filter { it.isSelected }.map { it.id }
        is NumericQuestion -> null
    },
    numeric = when (this) {
        is MultiChoiceQuestion -> null
        is NumericQuestion -> this.selectedValue
    }
)