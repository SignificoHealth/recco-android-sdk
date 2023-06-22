package com.recco.core.article.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.recco.core.article.ArticleRoute
import com.recco.core.model.recommendation.ContentId
import com.recco.core.ui.navigation.ContentIdNavType

internal const val idArg = "id"
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
            arguments = listOf(navArgument(idArg) { type = ContentIdNavType })
        ) {
            ArticleRoute(navigateUp)
        }
    }
}

fun NavController.navigateToArticle(
    contentId: ContentId
) {
    navigate(
        ArticleRoute.replace(
            oldValue = "{$idArg}",
            newValue = ContentIdNavType.serializeAsValue(contentId)
        )
    )
}
