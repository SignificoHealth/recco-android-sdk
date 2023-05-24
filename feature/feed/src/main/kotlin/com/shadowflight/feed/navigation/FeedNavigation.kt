package com.shadowflight.feed.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.shadowflight.feed.FeedRoute

const val FeedGraph = "feed_graph"
const val FeedRoute = "feed"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.feedGraph() {
    navigation(
        route = FeedGraph,
        startDestination = FeedRoute
    ) {
        composable(route = FeedRoute) {
            FeedRoute()
        }
    }
}
