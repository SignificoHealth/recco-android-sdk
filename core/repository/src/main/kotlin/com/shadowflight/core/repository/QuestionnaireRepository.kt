package com.shadowflight.core.repository

import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.questionnaire.Question
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.QuestionnaireApi
import com.shadowflight.core.openapi.model.QuestionDTO
import com.shadowflight.core.openapi.model.TopicDTO
import com.shadowflight.core.repository.mapper.asDTO
import com.shadowflight.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class QuestionnaireRepository @Inject constructor(
    private val api: QuestionnaireApi
) {
    suspend fun getQuestionnaireByTopic(topic: Topic): List<Question> =
        api.getQuestionnaireByTopic(topic.asDTO()).unwrap().map(QuestionDTO::asEntity)

    suspend fun getOnboarding(): List<Question> =
        api.onboarding().unwrap().map(QuestionDTO::asEntity)

    suspend fun answers(questions: List<Question>) {
        api.answers(questions.map(Question::asDTO))
    }

    suspend fun onboardingAnswers(questions: List<Question>) {
        api.onboardingAnswers(questions.map(Question::asDTO))
    }
}