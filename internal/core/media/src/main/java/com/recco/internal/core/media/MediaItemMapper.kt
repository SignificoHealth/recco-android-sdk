package com.recco.internal.core.media

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.TrackItem

fun Audio.asMediaItem(): MediaItem {
    val mediaMetaData = MediaMetadata.Builder().apply {
//        imageUrl?.let { setArtworkUri(Uri.parse(it)) }
        setTitle(headline)
    }.build()

    val trackUri = Uri.parse(audioUrl)

    return MediaItem.Builder()
        .setUri(trackUri)
        .setMediaId(id.itemId)
        .setMediaMetadata(mediaMetaData)
        .build()
}

fun Video.asMediaItem(): MediaItem {
    val mediaMetaData = MediaMetadata.Builder().apply {
//        imageUrl?.let { setArtworkUri(Uri.parse(it)) }
        setTitle(headline)
    }.build()

    val trackUri = Uri.parse(videoUrl)

    return MediaItem.Builder()
        .setUri(trackUri)
        .setMediaId(id.itemId)
        .setMediaMetadata(mediaMetaData)
        .build()
}

fun TrackItem.asMediaItem(): MediaItem {
    val mediaMetaData = MediaMetadata.Builder().apply {
//        imageUrl?.let { setArtworkUri(Uri.parse(it)) }
        setTitle(title)
    }.build()

    val trackUri = Uri.parse(audioUrl)

    return MediaItem.Builder()
        .setUri(trackUri)
        .setMediaId(id)
        .setMediaMetadata(mediaMetaData)
        .build()
}
