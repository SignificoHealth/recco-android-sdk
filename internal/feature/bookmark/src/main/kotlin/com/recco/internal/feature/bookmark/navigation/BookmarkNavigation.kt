package com.recco.internal.feature.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.recco.internal.feature.bookmark.BookmarkRoute
import com.recco.internal.core.model.recommendation.ContentId

private const val BookmarkGraph = "bookmark_graph"
const val BookmarkRoute = "bookmark"

fun NavGraphBuilder.bookmarkGraph(
    navigateToArticle: (ContentId) -> Unit,
    navigateUp: () -> Unit
) {
    navigation(
        route = BookmarkGraph,
        startDestination = BookmarkRoute
    ) {
        composable(route = BookmarkRoute) {
            BookmarkRoute(navigateToArticle, navigateUp)
        }
    }
}

fun NavController.navigateToBookmarks() {
    navigate(BookmarkRoute)
}
