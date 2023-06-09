package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Recommendation
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.openapi.model.AppUserRecommendationDTO

internal fun AppUserRecommendationDTO.asEntity() = Recommendation(
    id = id.asEntity(),
    rating = rating.asEntity(),
    status = status.asEntity(),
    headline = headline,
    lead = lead,
    imageUrl = imageUrl,
    bookmarked = bookmarked
)