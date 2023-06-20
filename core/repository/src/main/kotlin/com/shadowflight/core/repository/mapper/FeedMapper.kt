package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.openapi.model.FeedSectionDTO

internal fun FeedSectionDTO.asEntity() = FeedSection(
    type = type.asEntity(),
    locked = locked,
    topic = topic?.asEntity()
)