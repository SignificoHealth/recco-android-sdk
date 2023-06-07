package com.shadowflight.core.questionnaire

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.components.BackIconButton

@Composable
internal fun QuestionnaireRoute(topic: Topic, navigateUp: () -> Unit) {
    QuestionnaireScreen(topic, navigateUp)
}

@Composable
fun QuestionnaireScreen(topic: Topic, navigateUp: () -> Unit) {
    AppTopBar(
        title = "Topic: $topic",
        elevation = 0.dp,
        navigationIcon = {
            BackIconButton(onClick = navigateUp)
        }
    )
}