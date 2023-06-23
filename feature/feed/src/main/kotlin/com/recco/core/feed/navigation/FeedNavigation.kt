package com.recco.core.feed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.recco.core.feed.FeedRoute
import com.recco.core.model.feed.FeedSectionType
import com.recco.core.model.feed.Topic
import com.recco.core.model.recommendation.ContentId

const val FeedGraph = "feed_graph"
const val FeedRoute = "feed"

fun NavGraphBuilder.feedGraph(
    navigateToArticle: (ContentId) -> Unit,
    navigateToQuestionnaire: (Topic, FeedSectionType) -> Unit,
    navigateToBookmarks: () -> Unit
) {
    navigation(
        route = FeedGraph,
        startDestination = FeedRoute
    ) {
        composable(route = FeedRoute) {
            FeedRoute(navigateToArticle, navigateToQuestionnaire, navigateToBookmarks)
        }
    }
}

fun NavController.navigateToFeed() {
    navigate(FeedRoute) {
        popUpTo(id = 0)
    }
}
