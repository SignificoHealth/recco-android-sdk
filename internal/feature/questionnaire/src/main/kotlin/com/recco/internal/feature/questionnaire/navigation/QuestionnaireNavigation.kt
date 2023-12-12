package com.recco.internal.feature.questionnaire.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.ui.extensions.asSerializable
import com.recco.internal.feature.questionnaire.QuestionnaireOnboardingOutroRoute
import com.recco.internal.feature.questionnaire.QuestionnaireRoute

internal const val topicArg = "topic"
internal const val idArg = "id"
internal const val feedSectionTypeArg = "feedSectionType"
const val QuestionnaireGraph = "questionnaire_graph"
private const val QuestionnaireRoute = "questionnaire/{$topicArg}/{$feedSectionTypeArg}?$idArg={$idArg}"
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
                },
                navArgument(idArg) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            QuestionnaireRoute(
                topic = checkNotNull(backStackEntry.arguments?.asSerializable(topicArg)),
                navigateUp = navigateUp,
                navigateToOutro = navigateToOutro
            )
        }

        composable(
            route = QuestionnaireOnboardingRoute
        ) {
            QuestionnaireRoute(
                topic = null,
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
    feedSectionType: FeedSectionType,
    contentId: ContentId? = null
) {
    navigate(
        if (contentId != null) {
            QuestionnaireRoute.replaceAfter("$idArg=", contentId.itemId)
        } else {
            QuestionnaireRoute.replaceAfter("{$feedSectionTypeArg}", "")
        }.replace(
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
