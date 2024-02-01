package com.recco.internal.feature.media.description

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.TrackItem
import com.recco.internal.feature.media.asTrackItem

sealed class MediaDescriptionUI(
    val contentId: ContentId,
    val imageUrl: String?,
    val imageAlt: String?
) {
    data class VideoDescriptionUI(
        val video: Video
    ) : MediaDescriptionUI(
        contentId = video.id,
        imageAlt = video.imageAlt,
        imageUrl = video.imageUrl
    )

    data class AudioDescriptionUI(
        val audio: Audio
    ) : MediaDescriptionUI(
        contentId = audio.id,
        imageAlt = audio.imageAlt,
        imageUrl = audio.imageUrl
    )

    val contentType: ContentType
        get() = when {
            this is AudioDescriptionUI -> ContentType.AUDIO
            this is VideoDescriptionUI -> ContentType.VIDEO
            else -> error("Non supported ContentType: $this, for MediaDescriptionUi")
        }

    val trackItem: TrackItem
        get() = when (this) {
            is AudioDescriptionUI -> audio.asTrackItem()
            is VideoDescriptionUI -> video.asTrackItem()
        }
}
