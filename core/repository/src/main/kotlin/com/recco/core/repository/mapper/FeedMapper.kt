package com.recco.core.repository.mapper

import com.recco.core.model.feed.FeedSection
import com.recco.core.model.feed.FeedSectionState
import com.recco.core.openapi.model.FeedSectionDTO
import com.recco.core.openapi.model.FeedSectionStateDTO


internal fun FeedSectionDTO.asEntity() = FeedSection(
    type = type.asEntity(),
    state = when (state) {
        FeedSectionStateDTO.LOCK -> FeedSectionState.LOCKED
        FeedSectionStateDTO.UNLOCK -> FeedSectionState.UNLOCKED
        FeedSectionStateDTO.PARTIALLY_UNLOCK -> FeedSectionState.PARTIALLY_UNLOCKED
    },
    topic = topic?.asEntity()
)