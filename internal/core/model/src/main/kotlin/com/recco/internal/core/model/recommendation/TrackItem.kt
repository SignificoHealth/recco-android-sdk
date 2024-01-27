package com.recco.internal.core.model.recommendation

data class TrackItem (
    var id: String,
    var mediaUrl: String,
    var imageUrl: String?,
    var title: String,
    var lengthInMs: Long?,
)
