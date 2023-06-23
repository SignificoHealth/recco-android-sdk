package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.openapi.model.ContentIdDTO

fun ContentIdDTO.asEntity() = ContentId(itemId = itemId, catalogId = catalogId)
fun ContentId.asDTO() = ContentIdDTO(itemId = itemId, catalogId = catalogId)