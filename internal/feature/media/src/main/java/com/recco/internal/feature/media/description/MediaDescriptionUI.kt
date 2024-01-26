package com.recco.internal.feature.media.description

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.ui.components.UserInteractionRecommendation

sealed class MediaDescriptionUi(
    val contentId: ContentId,
    val imageUrl: String?,
    val imageAlt: String?,
    open val userInteraction: UserInteractionRecommendation
) {
    data class VideoDescriptionUi(
        override val userInteraction: UserInteractionRecommendation,
        val video: Video,
    ): MediaDescriptionUi(
        contentId = video.id,
        userInteraction = userInteraction,
        imageAlt = video.imageAlt,
        imageUrl = video.imageUrl
    )

    data class AudioDescriptionUi(
        override val userInteraction: UserInteractionRecommendation,
        val audio: Audio,
    ): MediaDescriptionUi(
        contentId = audio.id,
        userInteraction = userInteraction,
        imageAlt = audio.imageAlt,
        imageUrl = audio.imageUrl
    )

    val contentType: ContentType
        get() = when {
            this is AudioDescriptionUi -> ContentType.AUDIO
            this is VideoDescriptionUi -> ContentType.VIDEO
            else -> error("Non supported ContentType: $this, for MediaDescriptionUi")
        }
}
