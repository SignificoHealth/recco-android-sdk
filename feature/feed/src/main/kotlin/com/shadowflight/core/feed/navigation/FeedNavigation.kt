package com.shadowflight.core.feed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.shadowflight.core.feed.FeedRoute
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.recommendation.ContentId

const val FeedGraph = "feed_graph"
const val FeedRoute = "feed"

fun NavGraphBuilder.feedGraph(
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic) -> Unit
) {
    navigation(
        route = FeedGraph,
        startDestination = FeedRoute
    ) {
        composable(route = FeedRoute) {
            FeedRoute(navigateToArticle, navigateToQuestionnaire)
        }
    }
}

fun NavController.navigateToFeed() {
    navigate(FeedRoute) {
        popUpTo(id = 0)
    }
}
