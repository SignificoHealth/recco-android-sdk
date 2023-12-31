package com.recco.internal.feature.questionnaire

import kotlin.random.Random

sealed class QuestionnaireViewEvent {
    object QuestionnaireSubmitted : QuestionnaireViewEvent()
    object QuestionnaireOnboardingSubmitted : QuestionnaireViewEvent()
    data class ScrollTo(val page: Int, val id: Int = Random.nextInt()) : QuestionnaireViewEvent()
}
