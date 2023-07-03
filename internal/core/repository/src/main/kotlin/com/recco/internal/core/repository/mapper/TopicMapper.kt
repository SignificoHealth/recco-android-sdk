package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.openapi.model.TopicDTO

internal fun TopicDTO.asEntity() = when (this) {
    TopicDTO.PHYSICAL_ACTIVITY -> Topic.PHYSICAL_ACTIVITY
    TopicDTO.NUTRITION -> Topic.NUTRITION
    TopicDTO.MENTAL_WELLBEING -> Topic.MENTAL_WELLBEING
    TopicDTO.SLEEP -> Topic.SLEEP
}

internal fun Topic.asDTO() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> TopicDTO.PHYSICAL_ACTIVITY
    Topic.NUTRITION -> TopicDTO.NUTRITION
    Topic.MENTAL_WELLBEING -> TopicDTO.MENTAL_WELLBEING
    Topic.SLEEP -> TopicDTO.SLEEP
}