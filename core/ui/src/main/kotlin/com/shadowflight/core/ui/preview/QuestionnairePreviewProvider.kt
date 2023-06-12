package com.shadowflight.core.ui.preview

import com.shadowflight.core.model.questionnaire.MultiChoiceAnswerOption
import com.shadowflight.core.model.questionnaire.MultiChoiceQuestion
import com.shadowflight.core.model.questionnaire.NumericQuestion
import com.shadowflight.core.model.questionnaire.NumericQuestionFormat
import com.shadowflight.core.model.questionnaire.Questionnaire
import java.util.UUID

class QuestionnairePreviewProvider {
    companion object {
        fun multiChoice(maxOptions: Int = 2) = Questionnaire(
            id = UUID.randomUUID().toString(),
            questions = listOf(
                MultiChoiceQuestion(
                    id = UUID.randomUUID().toString(),
                    index = 0,
                    text = "Which of the following is the closest description of your work role?",
                    maxOptions = maxOptions,
                    minOptions = 1,
                    options = listOf(
                        MultiChoiceAnswerOption(id = 1, text = "Answer 1", isSelected = true),
                        MultiChoiceAnswerOption(id = 2, text = "Answer 2", isSelected = false)
                    )
                )
            )
        )

        fun numeric() = Questionnaire(
            id = UUID.randomUUID().toString(),
            questions = listOf(
                NumericQuestion(
                    id = UUID.randomUUID().toString(),
                    index = 0,
                    text = "Which of the following is the closest description of your work role?",
                    maxValue = 5,
                    minValue = 1,
                    selectedValue = null,
                    format = NumericQuestionFormat.HUMAN_HEIGHT
                )
            )
        )
    }
}