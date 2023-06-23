package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.openapi.model.FeedSectionTypeDTO

fun FeedSectionTypeDTO.asEntity() = when (this) {
    FeedSectionTypeDTO.PHYSICAL_ACTIVITY_RECOMMENDATIONS -> FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS
    FeedSectionTypeDTO.NUTRITION_RECOMMENDATIONS -> FeedSectionType.NUTRITION_RECOMMENDATIONS
    FeedSectionTypeDTO.PHYSICAL_WELLBEING_RECOMMENDATIONS -> FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS
    FeedSectionTypeDTO.SLEEP_RECOMMENDATIONS -> FeedSectionType.SLEEP_RECOMMENDATIONS
    FeedSectionTypeDTO.PREFERRED_RECOMMENDATIONS -> FeedSectionType.PREFERRED_RECOMMENDATIONS
    FeedSectionTypeDTO.MOST_POPULAR -> FeedSectionType.MOST_POPULAR
    FeedSectionTypeDTO.NEW_CONTENT -> FeedSectionType.NEW_CONTENT
    FeedSectionTypeDTO.PHYSICAL_ACTIVITY_EXPLORE -> FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE
    FeedSectionTypeDTO.NUTRITION_EXPLORE -> FeedSectionType.NUTRITION_EXPLORE
    FeedSectionTypeDTO.PHYSICAL_WELLBEING_EXPLORE -> FeedSectionType.MENTAL_WELLBEING_EXPLORE
    FeedSectionTypeDTO.SLEEP_EXPLORE -> FeedSectionType.SLEEP_EXPLORE
    FeedSectionTypeDTO.STARTING_RECOMMENDATIONS -> FeedSectionType.STARTING_RECOMMENDATIONS
}