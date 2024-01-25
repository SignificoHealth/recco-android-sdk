package com.recco.internal.core.media

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.TrackItem

fun Article.asTrackItem(): TrackItem {
    require(audioUrl != null) {
        "Cannot map an Article to TrackItem with a null audioUrl"
    }

    return TrackItem(
        id = id.itemId,
        audioUrl = checkNotNull(audioUrl),
        title = headline,
        imageUrl = imageUrl,
        lengthInMs = readingTimeInSeconds?.let {
            (it * 1000).toLong()
        }
    )
}
