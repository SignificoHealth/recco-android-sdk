package com.recco.internal.feature.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.feature.bookmark.BookmarkRoute

private const val BookmarkGraph = "bookmark_graph"
const val BookmarkRoute = "bookmark"

fun NavGraphBuilder.bookmarkGraph(
    navigateToArticle: (ContentId) -> Unit,
    navigateToMediaDescription: (ContentId, ContentType) -> Unit,
    navigateUp: () -> Unit
) {
    navigation(
        route = BookmarkGraph,
        startDestination = BookmarkRoute
    ) {
        composable(route = BookmarkRoute) {
            BookmarkRoute(navigateToArticle, navigateUp, navigateToMediaDescription)
        }
    }
}

fun NavController.navigateToBookmarks() {
    navigate(BookmarkRoute)
}
