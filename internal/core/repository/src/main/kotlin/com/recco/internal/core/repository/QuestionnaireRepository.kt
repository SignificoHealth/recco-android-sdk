package com.recco.internal.core.repository

import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.questionnaire.Question
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.network.http.unwrap
import com.recco.internal.core.openapi.api.QuestionnaireApi
import com.recco.internal.core.openapi.model.QuestionDTO
import com.recco.internal.core.repository.mapper.asDTO
import com.recco.internal.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class QuestionnaireRepository @Inject constructor(
    private val api: QuestionnaireApi
) {
    suspend fun getQuestionnaireByTopic(topic: Topic): List<Question> =
        api.getQuestionnaireByTopic(topic.asDTO()).unwrap().map(QuestionDTO::asEntity)

    suspend fun getQuestionnaireById(id: String): List<Question> =
        api.getQuestionnaire(id).unwrap().map(QuestionDTO::asEntity)

    suspend fun getOnboarding(): List<Question> =
        api.onboarding().unwrap().map(QuestionDTO::asEntity)

    suspend fun answers(questions: List<Question>) {
        api.answers(questions.map(Question::asDTO))
    }

    suspend fun onboardingAnswers(questions: List<Question>) {
        api.onboardingAnswers(questions.map(Question::asDTO))
    }
}
