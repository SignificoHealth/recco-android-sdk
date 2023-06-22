package com.recco.core.repository.mapper

import com.recco.core.model.recommendation.Status
import com.recco.core.openapi.model.StatusDTO

fun StatusDTO.asEntity() = when (this) {
    StatusDTO.NO_INTERACTION -> Status.NO_INTERACTION
    StatusDTO.VIEWED -> Status.VIEWED
}