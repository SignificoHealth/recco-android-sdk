package com.recco.internal.core.media.mapper

import com.recco.internal.core.model.media.MediaType
import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.TrackItem

fun Article.asTrackItemOrNull(): TrackItem? {
    return audioUrl?.let {
        TrackItem(
            id = id.itemId,
            mediaUrl = it,
            title = headline,
            imageUrl = imageUrl,
            mediaType = MediaType.AUDIO,
            lengthInMs = readingTimeInSeconds?.let { seconds ->
                (seconds * 1000).toLong()
            }
        )
    }
}
