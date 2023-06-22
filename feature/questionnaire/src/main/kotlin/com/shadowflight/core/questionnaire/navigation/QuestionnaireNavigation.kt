package com.shadowflight.core.questionnaire.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.questionnaire.QuestionnaireOnboardingOutroRoute
import com.shadowflight.core.questionnaire.QuestionnaireRoute
import com.shadowflight.core.ui.extensions.asSerializable

internal const val topicArg = "topic"
internal const val feedSectionTypeArg = "feedSectionType"
const val QuestionnaireGraph = "questionnaire_graph"
private const val QuestionnaireRoute = "questionnaire/{$topicArg}/{$feedSectionTypeArg}"
private const val QuestionnaireOnboardingRoute = "questionnaire_onboarding"
private const val QuestionnaireOnboardingOutroRoute = "questionnaire_onboarding_outro"

fun NavGraphBuilder.questionnaireGraph(
    isOnboardingQuestionnaireCompleted: Boolean,
    navigateUp: () -> Unit,
    navigateToFeed: () -> Unit,
    navigateToOutro: () -> Unit
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
                },
                navArgument(feedSectionTypeArg) {
                    type = NavType.EnumType(FeedSectionType::class.java)
                }
            )
        ) { backStackEntry ->
            QuestionnaireRoute(
                topic = checkNotNull(backStackEntry.arguments?.asSerializable(topicArg)),
                feedSectionType = checkNotNull(
                    backStackEntry.arguments?.asSerializable(feedSectionTypeArg)
                ),
                navigateUp = navigateUp,
                navigateToOutro = navigateToOutro
            )
        }
        composable(
            route = QuestionnaireOnboardingRoute
        ) {
            QuestionnaireRoute(
                topic = null,
                feedSectionType = null,
                navigateUp = navigateUp,
                navigateToOutro = navigateToOutro
            )
        }

        composable(
            route = QuestionnaireOnboardingOutroRoute
        ) {
            QuestionnaireOnboardingOutroRoute(
                navigateToFeed = navigateToFeed
            )
        }
    }
}

fun NavController.navigateToTopicQuestionnaire(
    topic: Topic,
    feedSectionType: FeedSectionType
) {
    navigate(
        QuestionnaireRoute.replace(
            oldValue = "{$topicArg}",
            newValue = topic.name
        ).replace(
            oldValue = "{$feedSectionTypeArg}",
            newValue = feedSectionType.name
        )
    )
}

fun NavController.navigateToOnboardingQuestionnaire() {
    navigate(QuestionnaireOnboardingRoute) {
        popUpTo(0)
    }
}

fun NavController.navigateToOnboardingQuestionnaireOutro() {
    navigate(QuestionnaireOnboardingOutroRoute) {
        popUpTo(0)
    }
}
