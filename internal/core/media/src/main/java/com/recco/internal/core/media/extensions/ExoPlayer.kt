@file:UnstableApi

package com.recco.internal.core.media.extensions

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.recco.internal.core.media.asMediaItem
import com.recco.internal.core.model.recommendation.TrackItem

fun ExoPlayer.prepareFor(
    context: Context,
    trackItem: TrackItem
) {
    val factory = DefaultDataSource.Factory(context)
    val source = HlsMediaSource.Factory(factory)
        .createMediaSource(trackItem.asMediaItem())
    setMediaSource(source)
    prepare()
}
