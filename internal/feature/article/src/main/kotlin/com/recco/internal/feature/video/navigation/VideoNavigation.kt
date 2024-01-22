package com.recco.internal.feature.video.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.ui.navigation.ContentIdNavType
import com.recco.internal.feature.video.VideoRoute

internal const val idArg = "id"
const val VideoGraph = "video_graph/{$idArg}"
private const val VideoRoute = "videoRoute/{$idArg}"

fun NavGraphBuilder.videoGraph(
    navigateUp: () -> Unit
) {
    navigation(
        route = VideoGraph,
        startDestination = VideoRoute
    ) {
        composable(
            route = VideoRoute,
            arguments = listOf(navArgument(idArg) { type = ContentIdNavType })
        ) {
            VideoRoute(navigateUp)
        }
    }
}

fun NavController.navigateToVideo(
    contentId: ContentId
) {
    navigate(
        VideoRoute.replace(
            oldValue = "{$idArg}",
            newValue = ContentIdNavType.serializeAsValue(contentId)
        )
    )
}
