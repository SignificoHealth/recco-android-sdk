package com.recco.internal.core.media

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.recco.internal.core.model.recommendation.TrackItem

fun TrackItem.asMediaItem(): MediaItem {
    val mediaMetaData = MediaMetadata.Builder().apply {
        setTitle(title)
    }.build()

    val trackUri = Uri.parse(mediaUrl)

    return MediaItem.Builder()
        .setUri(trackUri)
        .setMediaId(id)
        .setMediaMetadata(mediaMetaData)
        .build()
}
