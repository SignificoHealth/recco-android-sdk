package com.shadowflight.questionnaire

import androidx.compose.runtime.Composable
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.uicommons.components.AppTopBar

@Composable
internal fun QuestionnaireRoute(topic: Topic, navigateUp: () -> Unit) {
    QuestionnaireScreen(topic, navigateUp)
}

@Composable
fun QuestionnaireScreen(topic: Topic, navigateUp: () -> Unit) {
    AppTopBar(title = "Topic: $topic", navigateUp = navigateUp)
}