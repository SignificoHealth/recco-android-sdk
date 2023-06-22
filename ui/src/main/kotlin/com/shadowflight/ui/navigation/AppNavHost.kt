package com.shadowflight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.shadowflight.core.article.navigation.articleGraph
import com.shadowflight.core.article.navigation.navigateToArticle
import com.shadowflight.core.feed.navigation.FeedGraph
import com.shadowflight.core.feed.navigation.feedGraph
import com.shadowflight.core.feed.navigation.navigateToFeed
import com.shadowflight.core.model.recommendation.User
import com.shadowflight.core.onboarding.navigation.OnboardingGraph
import com.shadowflight.core.onboarding.navigation.onboardingGraph
import com.shadowflight.core.questionnaire.navigation.navigateToOnboardingQuestionnaire
import com.shadowflight.core.questionnaire.navigation.navigateToOnboardingQuestionnaireOutro
import com.shadowflight.core.questionnaire.navigation.navigateToTopicQuestionnaire
import com.shadowflight.core.questionnaire.navigation.questionnaireGraph

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