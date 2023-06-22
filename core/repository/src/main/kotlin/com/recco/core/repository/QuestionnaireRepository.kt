package com.recco.core.repository

import com.recco.core.model.feed.Topic
import com.recco.core.model.questionnaire.Question
import com.recco.core.network.http.unwrap
import com.recco.core.openapi.api.QuestionnaireApi
import com.recco.core.openapi.model.QuestionDTO
import com.recco.core.openapi.model.TopicDTO
import com.recco.core.repository.mapper.asDTO
import com.recco.core.repository.mapper.asEntity
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