package com.shadowflight.core.repository

import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.questionnaire.Questionnaire
import com.shadowflight.core.model.questionnaire.QuestionnaireAnswers
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.QuestionnaireApi
import com.shadowflight.core.openapi.model.QuestionnaireAnswersDTO
import com.shadowflight.core.openapi.model.QuestionnaireDTO
import com.shadowflight.core.repository.mapper.asDTO
import com.shadowflight.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

@ActivityRetainedScoped
class QuestionnaireRepository @Inject constructor(
    private val api: QuestionnaireApi
) {
    suspend fun getQuestionnaireByTopic(topic: Topic): Questionnaire =
        api.getQuestionnaireByTopic(topic.asDTO()).unwrap().asEntity()

    suspend fun onboarding(): List<Questionnaire> =
        api.onboarding().unwrap().map(QuestionnaireDTO::asEntity)

    suspend fun answers(questionnaireAnswers: QuestionnaireAnswers) {
        api.answers(questionnaireAnswers.asDTO()).unwrap()
    }

    suspend fun onboardingAnswers(questionnaireAnswers: List<QuestionnaireAnswers>) {
        api.onboardingAnswers(questionnaireAnswers.map(QuestionnaireAnswers::asDTO)).unwrap()
    }
}