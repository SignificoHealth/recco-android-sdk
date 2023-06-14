package com.shadowflight.core.questionnaire.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.recommendation.User
import com.shadowflight.core.questionnaire.QuestionnaireRoute
import com.shadowflight.core.ui.extensions.asSerializable

internal const val topicArg = "topic"
const val QuestionnaireGraph = "questionnaire_graph"
private const val QuestionnaireRoute = "questionnaire/{$topicArg}"
private const val QuestionnaireOnboardingRoute = "questionnaire_onboarding"

fun NavGraphBuilder.questionnaireGraph(
    isOnboardingQuestionnaireCompleted: Boolean,
    navigateUp: () -> Unit,
    navigateToFeed: () -> Unit
) {
    navigation(
        route = QuestionnaireGraph,
        startDestination = if (isOnboardingQuestionnaireCompleted) {
            QuestionnaireRoute
        } else {
            QuestionnaireOnboardingRoute
        }
    ) {
        composable(
            route = QuestionnaireRoute,
            arguments = listOf(
                navArgument(topicArg) {
                    type = NavType.EnumType(Topic::class.java)
                }
            )
        ) { backStackEntry ->
            QuestionnaireRoute(
                topic = checkNotNull(backStackEntry.arguments?.asSerializable(topicArg)),
                navigateUp = navigateUp,
                navigateToFeed = navigateToFeed
            )
        }
        composable(
            route = QuestionnaireOnboardingRoute
        ) {
            QuestionnaireRoute(
                topic = null,
                navigateUp = navigateUp,
                navigateToFeed = navigateToFeed
            )
        }
    }
}

fun NavController.navigateToTopicQuestionnaire(
    topic: Topic
) {
    navigate(
        QuestionnaireRoute.replace(
            oldValue = "{$topicArg}",
            newValue = topic.name
        )
    )
}

fun NavController.navigateToOnboardingQuestionnaire() {
    navigate(QuestionnaireOnboardingRoute) {
        popUpTo(0)
    }
}
