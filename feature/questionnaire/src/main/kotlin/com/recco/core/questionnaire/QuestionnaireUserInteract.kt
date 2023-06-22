package com.recco.core.questionnaire

import com.recco.core.model.questionnaire.MultiChoiceAnswerOption
import com.recco.core.model.questionnaire.MultiChoiceQuestion
import com.recco.core.model.questionnaire.NumericQuestion

sealed class QuestionnaireUserInteract {
    object Retry : QuestionnaireUserInteract()
    object BackClicked : QuestionnaireUserInteract()
    object NextClicked : QuestionnaireUserInteract()
    data class ClickOnMultiChoiceAnswerOption(
        val question: MultiChoiceQuestion,
        val option: MultiChoiceAnswerOption
    ) : QuestionnaireUserInteract()

    data class WriteOnNumericQuestion(
        val question: NumericQuestion,
        val value: String
    ) : QuestionnaireUserInteract()
}