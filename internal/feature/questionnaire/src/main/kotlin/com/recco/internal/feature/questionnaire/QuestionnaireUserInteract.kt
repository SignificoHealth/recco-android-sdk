package com.recco.internal.feature.questionnaire

import com.recco.internal.core.model.questionnaire.MultiChoiceAnswerOption
import com.recco.internal.core.model.questionnaire.MultiChoiceQuestion
import com.recco.internal.core.model.questionnaire.NumericQuestion

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