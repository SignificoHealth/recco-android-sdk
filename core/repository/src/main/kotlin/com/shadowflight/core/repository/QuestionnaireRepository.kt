package com.shadowflight.core.repository

import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.questionnaire.Questionnaire
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.QuestionnaireApi
import com.shadowflight.core.openapi.model.QuestionnaireDTO
import com.shadowflight.core.repository.mapper.asAnswersDTO
import com.shadowflight.core.repository.mapper.asDTO
import com.shadowflight.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class QuestionnaireRepository @Inject constructor(
    private val api: QuestionnaireApi
) {
    suspend fun getQuestionnaireByTopic(topic: Topic): Questionnaire =
        api.getQuestionnaireByTopic(topic.asDTO()).unwrap().asEntity()

    suspend fun onboarding(): List<Questionnaire> =
        api.onboarding().unwrap().map(QuestionnaireDTO::asEntity)

    suspend fun answers(questionnaire: Questionnaire) {
        api.answers(questionnaire.asAnswersDTO())
    }

    suspend fun onboardingAnswers(questionnaires: List<Questionnaire>) {
        api.onboardingAnswers(questionnaires.map(Questionnaire::asAnswersDTO))
    }
}