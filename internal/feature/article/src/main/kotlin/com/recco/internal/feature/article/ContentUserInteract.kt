package com.recco.internal.feature.article

import com.recco.internal.core.model.recommendation.ContentId

sealed class ContentUserInteract {
    data class ToggleBookmarkState(val contentId: ContentId) : ContentUserInteract()
    data class ToggleLikeState(val contentId: ContentId) : ContentUserInteract()
    data class ToggleDislikeState(val contentId: ContentId) : ContentUserInteract()
}
