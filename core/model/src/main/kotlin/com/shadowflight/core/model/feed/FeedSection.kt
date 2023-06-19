package com.shadowflight.core.model.feed

data class FeedSection(
    val type: FeedSectionType,
    val locked: LockType,
    val topic: Topic?
)
enum class LockType { LOCKED, UNLOCKING, UNLOCKED }
