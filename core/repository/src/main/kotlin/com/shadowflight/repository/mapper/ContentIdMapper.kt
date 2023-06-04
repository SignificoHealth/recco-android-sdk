package com.shadowflight.repository.mapper

import com.shadowflight.model.recommendation.ContentId
import com.shadowflight.openapi.model.ContentIdDTO

fun ContentIdDTO.asEntity() = ContentId(itemId = itemId, catalogId = catalogId)