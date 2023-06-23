package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Recommendation
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.openapi.model.AppUserRecommendationDTO

internal fun AppUserRecommendationDTO.asEntity() = Recommendation(
    id = id.asEntity(),
    rating = rating.asEntity(),
    status = status.asEntity(),
    headline = headline,
    lead = lead,
    imageUrl = imageUrl,
    bookmarked = bookmarked
)