package com.recco.internal.core.media

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.recco.internal.core.model.media.MediaType
import com.recco.internal.core.model.recommendation.TrackItem

fun TrackItem.asMediaItem(): MediaItem {
    val mediaMetaData = MediaMetadata.Builder()
        .setTitle(title)
        .apply {
            when (mediaType) {
                MediaType.AUDIO -> MediaMetadata.MEDIA_TYPE_MIXED
                MediaType.VIDEO -> MediaMetadata.MEDIA_TYPE_VIDEO
            }
        }
        .build()

    return MediaItem.Builder()
        .setUri(Uri.parse(mediaUrl))
        .setMediaId(id)
        .setMediaMetadata(mediaMetaData)
        .build()
}
