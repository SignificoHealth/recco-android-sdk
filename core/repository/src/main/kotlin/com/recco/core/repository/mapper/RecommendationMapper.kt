package com.recco.core.repository.mapper

import com.recco.core.model.recommendation.Rating
import com.recco.core.model.recommendation.Recommendation
import com.recco.core.model.recommendation.Status
import com.recco.core.openapi.model.AppUserRecommendationDTO

internal fun AppUserRecommendationDTO.asEntity() = Recommendation(
    id = id.asEntity(),
    rating = rating.asEntity(),
    status = status.asEntity(),
    headline = headline,
    lead = lead,
    imageUrl = imageUrl,
    bookmarked = bookmarked
)