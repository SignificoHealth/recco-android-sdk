package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.Recommendation
import com.recco.internal.core.openapi.model.AppUserRecommendationDTO
import com.recco.internal.core.openapi.model.ContentTypeDTO.ARTICLES
import com.recco.internal.core.openapi.model.ContentTypeDTO.AUDIOS
import com.recco.internal.core.openapi.model.ContentTypeDTO.QUESTIONNAIRES
import com.recco.internal.core.openapi.model.ContentTypeDTO.VIDEOS

internal fun AppUserRecommendationDTO.asEntity() = Recommendation(
    id = id.asEntity(),
    rating = rating.asEntity(),
    status = status.asEntity(),
    headline = headline,
    imageUrl = dynamicImageResizingUrl,
    bookmarked = bookmarked,
    imageAlt = imageAlt,
    type = when (type) {
        ARTICLES -> ContentType.ARTICLE
        QUESTIONNAIRES -> ContentType.QUESTIONNAIRE
        AUDIOS -> ContentType.AUDIO
        VIDEOS -> ContentType.VIDEO
    }
)
