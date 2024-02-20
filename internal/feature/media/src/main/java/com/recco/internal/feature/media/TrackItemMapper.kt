package com.recco.internal.feature.media

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.MediaType
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.TrackItem

fun Audio.asTrackItem(): TrackItem {
    return TrackItem(
        id = id.itemId,
        mediaUrl = audioUrl,
        imageUrl = imageUrl,
        title = headline,
        mediaType = MediaType.AUDIO,
        lengthInMs = lengthInSeconds?.times(1000L)

    )
}

fun Video.asTrackItem(): TrackItem {
    return TrackItem(
        id = id.itemId,
        mediaUrl = videoUrl,
        imageUrl = imageUrl,
        title = headline,
        mediaType = MediaType.VIDEO,
        lengthInMs = lengthInSeconds?.times(1000L)

    )
}
