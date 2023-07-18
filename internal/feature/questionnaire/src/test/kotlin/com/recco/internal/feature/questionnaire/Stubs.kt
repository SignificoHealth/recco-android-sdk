package com.recco.internal.feature.questionnaire

import androidx.lifecycle.SavedStateHandle
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.questionnaire.Question
import com.recco.internal.core.repository.QuestionnaireRepository
import com.recco.internal.core.test.utils.staticThrowableForTesting
import com.recco.internal.feature.questionnaire.navigation.feedSectionTypeArg
import com.recco.internal.feature.questionnaire.navigation.topicArg
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.stub

internal fun SavedStateHandle.stub() {
    stub {
        on { it.get<Topic>(topicArg) } doReturn (Topic.SLEEP)
        on { it.get<FeedSectionType>(feedSectionTypeArg) } doReturn (FeedSectionType.SLEEP_RECOMMENDATIONS)
    }
}

internal fun QuestionnaireRepository.stubForInitialFailure() {
    stub {
        onBlocking { it.getOnboarding() } doThrow staticThrowableForTesting
        onBlocking { it.getQuestionnaireByTopic(any()) } doThrow staticThrowableForTesting
    }
}

internal fun QuestionnaireRepository.stubForInitialSuccess(mockAnswers: List<Question>) {
    stub {
        onBlocking { it.getOnboarding() } doReturn mockAnswers
        onBlocking { it.getQuestionnaireByTopic(any()) } doReturn mockAnswers
    }
}

internal fun QuestionnaireRepository.stubForSendAnswersSuccess() {
    stub {
        onBlocking { it.answers(any()) } doReturn Unit
        onBlocking { it.onboardingAnswers(any()) } doReturn Unit
    }
}

internal fun QuestionnaireRepository.stubForSendAnswersFailure() {
    stub {
        onBlocking { it.answers(any()) } doThrow staticThrowableForTesting
        onBlocking { it.onboardingAnswers(any()) } doThrow staticThrowableForTesting
    }
}
