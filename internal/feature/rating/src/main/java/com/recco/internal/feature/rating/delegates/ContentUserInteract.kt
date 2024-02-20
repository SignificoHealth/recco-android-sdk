package com.recco.internal.feature.rating.delegates

import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType

sealed class ContentUserInteract {
    data class ToggleBookmarkState(
        val contentId: ContentId,
        val contentType: ContentType
    ) : ContentUserInteract()
    data class ToggleLikeState(
        val contentId: ContentId,
        val contentType: ContentType
    ) : ContentUserInteract()
    data class ToggleDislikeState(
        val contentId: ContentId,
        val contentType: ContentType
    ) : ContentUserInteract()
}
