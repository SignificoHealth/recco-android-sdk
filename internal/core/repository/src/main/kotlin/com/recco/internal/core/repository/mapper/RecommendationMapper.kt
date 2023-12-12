package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.Recommendation
import com.recco.internal.core.openapi.model.AppUserRecommendationDTO
import com.recco.internal.core.openapi.model.ContentTypeDTO

internal fun AppUserRecommendationDTO.asEntity() = Recommendation(
    id = id.asEntity(),
    rating = rating.asEntity(),
    status = status.asEntity(),
    headline = headline,
    lead = lead,
    imageUrl = dynamicImageResizingUrl,
    bookmarked = bookmarked,
    imageAlt = imageAlt,
    type = when (type) {
        ContentTypeDTO.ARTICLES -> ContentType.ARTICLE
        ContentTypeDTO.QUESTIONNAIRES -> ContentType.QUESTIONNAIRE
    }
)
