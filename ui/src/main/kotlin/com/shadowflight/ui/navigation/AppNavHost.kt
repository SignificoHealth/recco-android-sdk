package com.shadowflight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.shadowflight.core.article.navigation.articleGraph
import com.shadowflight.core.article.navigation.navigateToArticle
import com.shadowflight.core.feed.navigation.FeedGraph
import com.shadowflight.core.feed.navigation.feedGraph
import com.shadowflight.onboarding.navigation.onboardingGraph
import com.shadowflight.questionnaire.navigation.navigateToQuestionnaire
import com.shadowflight.questionnaire.navigation.questionnaireGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = FeedGraph
    ) {
        onboardingGraph()
        feedGraph(
            navigateToArticle = navController::navigateToArticle,
            navigateToQuestionnaire = navController::navigateToQuestionnaire
        )
        articleGraph(navigateUp = navController::navigateUp)
        questionnaireGraph(navigateUp = navController::navigateUp)
    }
}