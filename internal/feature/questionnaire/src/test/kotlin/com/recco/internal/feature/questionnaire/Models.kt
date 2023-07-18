package com.recco.internal.feature.questionnaire

import com.recco.internal.core.ui.preview.QuestionnairePreviewProvider

internal val multiChoiceQuestion =
    QuestionnairePreviewProvider.multiChoice(isFirstSelected = false).first()

internal val numericQuestion = QuestionnairePreviewProvider.numeric().first()

internal val questions = listOf(multiChoiceQuestion, numericQuestion)
