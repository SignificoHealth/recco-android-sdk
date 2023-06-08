package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.questionnaire.MultiChoiceAnswerOption
import com.shadowflight.core.model.questionnaire.MultiChoiceQuestion
import com.shadowflight.core.model.questionnaire.NumericQuestion
import com.shadowflight.core.model.questionnaire.NumericQuestionFormat
import com.shadowflight.core.model.questionnaire.Question
import com.shadowflight.core.model.questionnaire.QuestionType
import com.shadowflight.core.model.questionnaire.Questionnaire
import com.shadowflight.core.model.questionnaire.QuestionnaireAnswer
import com.shadowflight.core.model.questionnaire.QuestionnaireAnswers
import com.shadowflight.core.openapi.model.CreateQuestionnaireAnswerDTO
import com.shadowflight.core.openapi.model.MultiChoiceAnswerOptionDTO
import com.shadowflight.core.openapi.model.MultiChoiceQuestionDTO
import com.shadowflight.core.openapi.model.NumericQuestionDTO
import com.shadowflight.core.openapi.model.QuestionDTO
import com.shadowflight.core.openapi.model.QuestionnaireAnswersDTO
import com.shadowflight.core.openapi.model.QuestionnaireDTO

fun QuestionnaireDTO.asEntity() = Questionnaire(
    id = id,
    questions = questions.map(QuestionDTO::asEntity)
)

private fun QuestionDTO.asEntity() = Question(
    id = id,
    index = index,
    text = text,
    type = type.asEntity(),
    multiChoice = multiChoice?.asEntity(),
    numeric = numeric?.asEntity()
)

private fun QuestionDTO.Type.asEntity() = when (this) {
    QuestionDTO.Type.MULTICHOICE -> QuestionType.MULTI_CHOICE
    QuestionDTO.Type.NUMERIC -> QuestionType.NUMERIC
}

private fun MultiChoiceQuestionDTO.asEntity() = MultiChoiceQuestion(
    maxOptions = maxOptions,
    minOptions = minOptions,
    options = options.map(MultiChoiceAnswerOptionDTO::asEntity)
)

private fun MultiChoiceAnswerOptionDTO.asEntity() = MultiChoiceAnswerOption(
    id = id,
    text = text
)

private fun NumericQuestionDTO.asEntity() = NumericQuestion(
    maxValue = maxValue,
    minValue = minValue,
    format = format.asEntity()
)

private fun NumericQuestionDTO.Format.asEntity() = when (this) {
    NumericQuestionDTO.Format.HUMAN_HEIGHT -> NumericQuestionFormat.HUMAN_HEIGHT
    NumericQuestionDTO.Format.HUMAN_WEIGHT -> NumericQuestionFormat.HUMAN_WEIGHT
    NumericQuestionDTO.Format.INTEGER -> NumericQuestionFormat.INTEGER
    NumericQuestionDTO.Format.DECIMAL -> NumericQuestionFormat.DECIMAL
}

fun QuestionnaireAnswers.asDTO() = QuestionnaireAnswersDTO(
    id = id,
    answers = answers.map(QuestionnaireAnswer::asDTO)
)

private fun QuestionnaireAnswer.asDTO() = CreateQuestionnaireAnswerDTO(
    questionId = questionId,
    type = type.asDTO(),
    multichoice = multiChoice,
    numeric = numeric
)

private fun QuestionType.asDTO() = when (this) {
    QuestionType.MULTI_CHOICE -> CreateQuestionnaireAnswerDTO.Type.MULTICHOICE
    QuestionType.NUMERIC -> CreateQuestionnaireAnswerDTO.Type.NUMERIC
}
