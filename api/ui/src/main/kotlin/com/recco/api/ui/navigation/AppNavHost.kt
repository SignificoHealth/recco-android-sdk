package com.recco.api.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.recco.internal.core.model.recommendation.User
import com.recco.internal.feature.article.navigation.articleGraph
import com.recco.internal.feature.article.navigation.navigateToArticle
import com.recco.internal.feature.bookmark.navigation.bookmarkGraph
import com.recco.internal.feature.bookmark.navigation.navigateToBookmarks
import com.recco.internal.feature.feed.navigation.FeedGraph
import com.recco.internal.feature.feed.navigation.feedGraph
import com.recco.internal.feature.feed.navigation.navigateToFeed
import com.recco.internal.feature.media.navigation.mediaGraph
import com.recco.internal.feature.media.navigation.navigateToMediaDescription
import com.recco.internal.feature.media.navigation.navigateToMediaPlayer
import com.recco.internal.feature.onboarding.navigation.OnboardingGraph
import com.recco.internal.feature.onboarding.navigation.onboardingGraph
import com.recco.internal.feature.questionnaire.navigation.navigateToOnboardingQuestionnaire
import com.recco.internal.feature.questionnaire.navigation.navigateToOnboardingQuestionnaireOutro
import com.recco.internal.feature.questionnaire.navigation.navigateToTopicQuestionnaire
import com.recco.internal.feature.questionnaire.navigation.questionnaireGraph

@Composable
internal fun AppNavHost(
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
            navigateToQuestionnaire = navController::navigateToTopicQuestionnaire,
            navigateToBookmarks = navController::navigateToBookmarks,
            navigateToMediaDescription = navController::navigateToMediaDescription
        )
        articleGraph(navigateUp = navController::navigateUp)
        questionnaireGraph(
            isOnboardingQuestionnaireCompleted = user.isOnboardingQuestionnaireCompleted,
            navigateUp = navController::navigateUp,
            navigateToFeed = navController::navigateToFeed,
            navigateToOutro = navController::navigateToOnboardingQuestionnaireOutro
        )
        bookmarkGraph(
            navigateToArticle = navController::navigateToArticle,
            navigateToMediaDescription = navController::navigateToMediaDescription,
            navigateUp = navController::navigateUp
        )
        mediaGraph(
            navigateUp = navController::navigateUp,
            navigateToMediaPlayer = navController::navigateToMediaPlayer
        )
    }
}
