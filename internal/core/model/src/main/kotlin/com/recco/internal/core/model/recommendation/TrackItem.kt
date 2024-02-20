package com.recco.internal.core.model.recommendation

import com.recco.internal.core.model.media.MediaType

data class TrackItem(
    val id: String,
    val mediaUrl: String,
    val imageUrl: String?,
    val title: String,
    val lengthInMs: Long?,
    val mediaType: MediaType
)
