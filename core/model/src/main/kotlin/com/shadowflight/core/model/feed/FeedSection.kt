package com.shadowflight.core.model.feed

data class FeedSection(
    val type: FeedSectionType,
    val locked: Boolean,
    val topic: Topic?
)