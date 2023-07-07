package com.recco.internal.feature.questionnaire.utils

import com.recco.internal.core.model.questionnaire.Question
import com.recco.internal.core.repository.QuestionnaireRepository
import com.recco.internal.core.test.utils.staticThrowableForTesting
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.stub

internal fun QuestionnaireRepository.stubForInitialFailure() {
    this.stub {
        onBlocking { it.getOnboarding() } doThrow staticThrowableForTesting
        onBlocking { it.getQuestionnaireByTopic(any()) } doThrow staticThrowableForTesting
    }
}

internal fun QuestionnaireRepository.stubForInitialSuccess(mockAnswers: List<Question>) {
    this.stub {
        onBlocking { it.getOnboarding() } doReturn mockAnswers
        onBlocking { it.getQuestionnaireByTopic(any()) } doReturn mockAnswers
    }
}

internal fun QuestionnaireRepository.stubForSendAnswersSuccess() {
    this.stub {
        onBlocking { it.answers(any()) } doReturn Unit
        onBlocking { it.onboardingAnswers(any()) } doReturn Unit
    }
}

internal fun QuestionnaireRepository.stubForSendAnswersFailure() {
    this.stub {
        onBlocking { it.answers(any()) } doThrow staticThrowableForTesting
        onBlocking { it.onboardingAnswers(any()) } doThrow staticThrowableForTesting
    }
}
