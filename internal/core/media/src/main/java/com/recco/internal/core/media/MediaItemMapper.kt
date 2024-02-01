package com.recco.internal.core.media

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.SubtitleConfiguration
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
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

    val trackUri = Uri.parse(mediaUrl)

    return MediaItem.Builder()
        .setUri(trackUri)
        .setMediaId(id)
        .setMediaMetadata(mediaMetaData)
        .apply {
            if (mediaType == MediaType.VIDEO) {
                setSubtitleConfigurations(
                    listOf(
                        SubtitleConfiguration.Builder(trackUri)
                            .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                            .build()
                    )
                )
            }
        }
        .build()
}
