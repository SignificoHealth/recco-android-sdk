package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.openapi.model.StatusDTO

fun StatusDTO.asEntity() = when (this) {
    StatusDTO.NO_INTERACTION -> Status.NO_INTERACTION
    StatusDTO.VIEWED -> Status.VIEWED
}