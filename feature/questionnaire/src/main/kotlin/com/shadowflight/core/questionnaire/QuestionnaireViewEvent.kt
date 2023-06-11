package com.shadowflight.core.questionnaire

import kotlin.random.Random

sealed class QuestionnaireViewEvent {
    object Initial : QuestionnaireViewEvent()
    object QuestionnaireSubmitted : QuestionnaireViewEvent()
    data class ScrollTo(val page: Int, val id: Int = Random.nextInt()) : QuestionnaireViewEvent()
}