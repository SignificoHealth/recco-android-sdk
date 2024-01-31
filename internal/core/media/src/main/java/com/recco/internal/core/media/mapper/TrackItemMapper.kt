package com.recco.internal.core.media.mapper

import com.recco.internal.core.model.media.MediaType
import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.TrackItem

fun Article.asTrackItem(): TrackItem {
    require(audioUrl != null) {
        "Cannot map an Article to TrackItem with a null audioUrl"
    }

    return TrackItem(
        id = id.itemId,
        mediaUrl = checkNotNull(audioUrl),
        title = headline,
        imageUrl = imageUrl,
        mediaType = MediaType.AUDIO,
        lengthInMs = readingTimeInSeconds?.let {
            (it * 1000).toLong()
        }
    )
}