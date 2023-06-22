package com.recco.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.recco.core.article.navigation.articleGraph
import com.recco.core.article.navigation.navigateToArticle
import com.recco.core.feed.navigation.FeedGraph
import com.recco.core.feed.navigation.feedGraph
import com.recco.core.feed.navigation.navigateToFeed
import com.recco.core.model.recommendation.User
import com.recco.core.onboarding.navigation.OnboardingGraph
import com.recco.core.onboarding.navigation.onboardingGraph
import com.recco.core.questionnaire.navigation.navigateToOnboardingQuestionnaire
import com.recco.core.questionnaire.navigation.navigateToOnboardingQuestionnaireOutro
import com.recco.core.questionnaire.navigation.navigateToTopicQuestionnaire
import com.recco.core.questionnaire.navigation.questionnaireGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    user: User
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = when {
            !user.isOnboardingQuestionnaireCompleted -> OnboardingGraph
            else -> FeedGraph
        }
    ) {
        onboardingGraph(navigateToQuestionnaire = navController::navigateToOnboardingQuestionnaire)
        feedGraph(
            navigateToArticle = navController::navigateToArticle,
            navigateToQuestionnaire = navController::navigateToTopicQuestionnaire
        )
        articleGraph(navigateUp = navController::navigateUp)
        questionnaireGraph(
            isOnboardingQuestionnaireCompleted = user.isOnboardingQuestionnaireCompleted,
            navigateUp = navController::navigateUp,
            navigateToFeed = navController::navigateToFeed,
            navigateToOutro = navController::navigateToOnboardingQuestionnaireOutro
        )
    }
}