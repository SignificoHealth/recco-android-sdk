package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.openapi.model.StatusDTO

fun StatusDTO.asEntity() = when (this) {
    StatusDTO.NO_INTERACTION -> Status.NO_INTERACTION
    StatusDTO.VIEWED -> Status.VIEWED
}