package com.shadowflight.article.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.shadowflight.article.ArticleRoute

private const val idArg = "id"

const val ArticleGraph = "article_graph/{$idArg}"
private const val ArticleRoute = "article/{$idArg}"

fun NavGraphBuilder.articleGraph(
    navigateUp: () -> Unit
) {
    navigation(
        route = ArticleGraph,
        startDestination = ArticleRoute
    ) {
        composable(
            route = ArticleRoute,
            arguments = listOf(navArgument(idArg) { type = NavType.StringType })
        ) { backStackEntry ->
            ArticleRoute(
                articleId = checkNotNull(backStackEntry.arguments?.getString(idArg)),
                navigateUp = navigateUp
            )
        }
    }
}

fun NavController.navigateToArticle(
    id: String
) {
    navigate(
        ArticleRoute.replace(
            oldValue = "{$idArg}",
            newValue = id
        )
    )
}
