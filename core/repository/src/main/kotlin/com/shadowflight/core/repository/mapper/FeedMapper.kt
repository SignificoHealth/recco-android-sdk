package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.model.feed.FeedSectionState
import com.shadowflight.core.openapi.model.FeedSectionDTO
import com.shadowflight.core.openapi.model.FeedSectionStateDTO


internal fun FeedSectionDTO.asEntity() = FeedSection(
    type = type.asEntity(),
    state = when (state) {
        FeedSectionStateDTO.LOCK -> FeedSectionState.LOCKED
        FeedSectionStateDTO.UNLOCK -> FeedSectionState.UNLOCKED
        FeedSectionStateDTO.PARTIALLY_UNLOCK -> FeedSectionState.PARTIALLY_UNLOCKED
    },
    topic = topic?.asEntity()
)