package com.recco.internal.feature.media.description

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.ui.components.UserInteractionRecommendation

sealed class MediaDescriptionUi(
    val imageUrl: String?,
    val imageAlt: String?,
    open val userInteraction: UserInteractionRecommendation
) {
    data class VideoDescriptionUi(
        override val userInteraction: UserInteractionRecommendation,
        val video: Video,
    ): MediaDescriptionUi(
        userInteraction = userInteraction,
        imageAlt = video.imageAlt,
        imageUrl = video.imageUrl
    )

    data class AudioDescriptionUi(
        override val userInteraction: UserInteractionRecommendation,
        val audio: Audio,
    ): MediaDescriptionUi(
        userInteraction = userInteraction,
        imageAlt = audio.imageAlt,
        imageUrl = audio.imageUrl
    )

    val contentType: ContentType
        get() = when {
            isAudio -> ContentType.AUDIO
            isVideo -> ContentType.VIDEO
            else -> error("Non supported content type for MediaDescriptionUi")
        }

    val isAudio: Boolean
        get() = this is AudioDescriptionUi

    val isVideo: Boolean
        get() = this is VideoDescriptionUi

}
