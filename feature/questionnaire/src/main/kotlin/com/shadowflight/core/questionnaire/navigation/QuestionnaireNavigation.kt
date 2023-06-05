package com.shadowflight.core.questionnaire.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.questionnaire.QuestionnaireRoute
import com.shadowflight.core.ui.extensions.asSerializable

private const val topicArg = "topic"

const val QuestionnaireGraph = "questionnaire_graph/{$topicArg}"
private const val QuestionnaireRoute = "questionnaire/{$topicArg}"

fun NavGraphBuilder.questionnaireGraph(
    navigateUp: () -> Unit
) {
    navigation(
        route = QuestionnaireGraph,
        startDestination = QuestionnaireRoute
    ) {
        composable(
            route = QuestionnaireRoute,
            arguments = listOf(
                navArgument(topicArg) { type = NavType.EnumType(Topic::class.java) }
            )
        ) { backStackEntry ->
            QuestionnaireRoute(
                topic = checkNotNull(backStackEntry.arguments?.asSerializable(topicArg)),
                navigateUp = navigateUp
            )
        }
    }
}

fun NavController.navigateToQuestionnaire(
    topic: Topic
) {
    navigate(
        QuestionnaireRoute.replace(
            oldValue = "{$topicArg}",
            newValue = topic.name
        )
    )
}
