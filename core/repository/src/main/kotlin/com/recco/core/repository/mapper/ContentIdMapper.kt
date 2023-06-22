package com.recco.core.repository.mapper

import com.recco.core.model.recommendation.ContentId
import com.recco.core.openapi.model.ContentIdDTO

fun ContentIdDTO.asEntity() = ContentId(itemId = itemId, catalogId = catalogId)
fun ContentId.asDTO() = ContentIdDTO(itemId = itemId, catalogId = catalogId)