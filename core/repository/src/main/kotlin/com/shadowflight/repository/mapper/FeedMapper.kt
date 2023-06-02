package com.shadowflight.repository.mapper

import com.shadowflight.model.feed.FeedSection
import com.shadowflight.model.feed.FeedSectionType
import com.shadowflight.openapi.model.FeedSectionDTO
import com.shadowflight.openapi.model.TopicDTO

internal fun FeedSectionDTO.asEntity() = FeedSection(
    type = when (type) {
        FeedSectionDTO.Type.PHYSICAL_ACTIVITY_RECOMMENDATIONS -> FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS
        FeedSectionDTO.Type.NUTRITION_RECOMMENDATIONS -> FeedSectionType.NUTRITION_RECOMMENDATIONS
        FeedSectionDTO.Type.PHYSICAL_WELLBEING_RECOMMENDATIONS -> FeedSectionType.PHYSICAL_WELLBEING_RECOMMENDATIONS
        FeedSectionDTO.Type.SLEEP_RECOMMENDATIONS -> FeedSectionType.SLEEP_RECOMMENDATIONS
        FeedSectionDTO.Type.PREFERRED_RECOMMENDATIONS -> FeedSectionType.PREFERRED_RECOMMENDATIONS
        FeedSectionDTO.Type.MOST_POPULAR -> FeedSectionType.MOST_POPULAR
        FeedSectionDTO.Type.NEW_CONTENT -> FeedSectionType.NEW_CONTENT
        FeedSectionDTO.Type.PHYSICAL_ACTIVITY_EXPLORE -> FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE
        FeedSectionDTO.Type.NUTRITION_EXPLORE -> FeedSectionType.NUTRITION_EXPLORE
        FeedSectionDTO.Type.PHYSICAL_WELLBEING_EXPLORE -> FeedSectionType.PHYSICAL_WELLBEING_EXPLORE
        FeedSectionDTO.Type.SLEEP_EXPLORE -> FeedSectionType.SLEEP_EXPLORE
        FeedSectionDTO.Type.STARTING_RECOMMENDATIONS -> FeedSectionType.STARTING_RECOMMENDATIONS
    },
    locked = locked,
    topic = topic?.asEntity()
)