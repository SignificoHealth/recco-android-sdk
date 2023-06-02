package com.shadowflight.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.shadowflight.feed.FeedRoute
import com.shadowflight.model.feed.Topic

const val FeedGraph = "feed_graph"
const val FeedRoute = "feed"

fun NavGraphBuilder.feedGraph(
    navigateToArticle: (id: String) -> Unit,
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
