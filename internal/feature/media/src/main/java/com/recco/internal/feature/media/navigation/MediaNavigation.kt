package com.recco.internal.feature.media.navigation

import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.ui.navigation.ContentIdNavType
import com.recco.internal.feature.media.description.MediaDescriptionRoute
import com.recco.internal.feature.media.player.FullMediaPlayerRoute

internal const val idArg = "id"
internal const val contentTypeArg = "content_type"
const val MediaGraph = "media/$contentTypeArg/{$idArg}"
private const val MediaPlayerRoute = "media_player/{$contentTypeArg}/{$idArg}"
private const val MediaDescriptionRoute = "media_description/{$contentTypeArg}/{$idArg}"

fun NavGraphBuilder.mediaGraph(
    navigateUp: () -> Unit,
    navigateToMediaPlayer: (ContentId, ContentType) -> Unit,
) {
    navigation(
        route = MediaGraph,
        startDestination = MediaPlayerRoute
    ) {
        composable(
            route = MediaDescriptionRoute,
            arguments = listOf(
                navArgument(idArg) { type = ContentIdNavType },
                navArgument(contentTypeArg) {
                    type = NavType.EnumType(ContentType::class.java)
                }
            )
        ) {
            MediaDescriptionRoute(
                navigateUp = navigateUp,
                navigateToMediaPlayer = navigateToMediaPlayer
            )
        }
        composable(
            route = MediaPlayerRoute,
            exitTransition = { ExitTransition.None }, // Disable default fadeOut() transition
            arguments = listOf(
                navArgument(idArg) { type = ContentIdNavType },
                navArgument(contentTypeArg) {
                    type = NavType.EnumType(ContentType::class.java)
                }
            )
        ) {
            FullMediaPlayerRoute(navigateUp)
        }
    }
}

fun NavController.navigateToMediaDescription(
    contentId: ContentId,
    contentType: ContentType
) {
    navigate(
        MediaDescriptionRoute
            .replace(
                oldValue = "{$idArg}",
                newValue = ContentIdNavType.serializeAsValue(contentId)
            )
            .replace(
                oldValue = "{$contentTypeArg}",
                newValue = contentType.toString()
            )
    )
}

fun NavController.navigateToMediaPlayer(
    contentId: ContentId,
    contentType: ContentType
) {
    navigate(
        MediaPlayerRoute
            .replace(
                oldValue = "{$idArg}",
                newValue = ContentIdNavType.serializeAsValue(contentId)
            )
            .replace(
                oldValue = "{$contentTypeArg}",
                newValue = contentType.toString()
            )
    )
}
