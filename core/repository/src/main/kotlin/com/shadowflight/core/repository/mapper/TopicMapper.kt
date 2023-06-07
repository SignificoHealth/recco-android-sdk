package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.openapi.model.TopicDTO

internal fun TopicDTO.asEntity() = when (this) {
    TopicDTO.PHYSICAL_ACTIVITY -> Topic.PHYSICAL_ACTIVITY
    TopicDTO.NUTRITION -> Topic.NUTRITION
    TopicDTO.PHYSICAL_WELLBEING -> Topic.PHYSICAL_WELLBEING
    TopicDTO.SLEEP -> Topic.SLEEP
}

internal fun Topic.asDTO() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> TopicDTO.PHYSICAL_ACTIVITY
    Topic.NUTRITION -> TopicDTO.NUTRITION
    Topic.PHYSICAL_WELLBEING -> TopicDTO.PHYSICAL_WELLBEING
    Topic.SLEEP -> TopicDTO.SLEEP
}