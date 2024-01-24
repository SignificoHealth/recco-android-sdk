package com.recco.internal.feature.media.video.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.ui.navigation.ContentIdNavType
import com.recco.internal.feature.media.player.FullMediaPlayerRoute

internal const val idArg = "id"
const val AudioGraph = "audio_graph/{$idArg}"
private const val FullAudioPlayerRoute = "fullAudioRoute/{$idArg}"

fun NavGraphBuilder.mediaGraph(
    navigateUp: () -> Unit
) {
    navigation(
        route = AudioGraph,
        startDestination = FullAudioPlayerRoute
    ) {
        composable(
            route = FullAudioPlayerRoute,
            arguments = listOf(navArgument(idArg) { type = ContentIdNavType })
        ) {
            FullMediaPlayerRoute(navigateUp)
        }
    }
}

fun NavController.navigateToFullAudioPlayer(
    contentId: ContentId
) {
    navigate(
        FullAudioPlayerRoute.replace(
            oldValue = "{$idArg}",
            newValue = ContentIdNavType.serializeAsValue(contentId)
        )
    )
}