package com.recco.internal.feature.media.description

internal sealed class MediaDescriptionUserInteract {
    data object OnPlayMedia : MediaDescriptionUserInteract()
    data object Retry : MediaDescriptionUserInteract()
    data object ToggleBookmarkState : MediaDescriptionUserInteract()
    data object ToggleLikeState : MediaDescriptionUserInteract()
    data object ToggleDislikeState : MediaDescriptionUserInteract()
}
