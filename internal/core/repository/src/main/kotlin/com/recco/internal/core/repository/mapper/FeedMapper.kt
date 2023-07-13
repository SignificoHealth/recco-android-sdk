package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.feed.FeedSection
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.openapi.model.FeedSectionDTO
import com.recco.internal.core.openapi.model.FeedSectionStateDTO

internal fun FeedSectionDTO.asEntity() = FeedSection(
    type = type.asEntity(),
    state = when (state) {
        FeedSectionStateDTO.LOCK -> FeedSectionState.LOCKED
        FeedSectionStateDTO.UNLOCK -> FeedSectionState.UNLOCKED
        FeedSectionStateDTO.PARTIALLY_UNLOCK -> FeedSectionState.PARTIALLY_UNLOCKED
    },
    topic = topic?.asEntity()
)