package com.shadowflight.repository.mapper

import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Recommendation
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.openapi.model.AppUserRecommendationDTO

internal fun AppUserRecommendationDTO.asEntity() = Recommendation(
    id = id.asEntity(),
    rating = when (rating) {
        AppUserRecommendationDTO.Rating.LIKE -> Rating.LIKE
        AppUserRecommendationDTO.Rating.DISLIKE -> Rating.DISLIKE
        AppUserRecommendationDTO.Rating.NOT_RATED -> Rating.NOT_RATED
    },
    status = when (status) {
        AppUserRecommendationDTO.Status.NO_INTERACTION -> Status.NO_INTERACTION
        AppUserRecommendationDTO.Status.VIEWED -> Status.VIEWED
    },
    headline = headline,
    lead = lead,
    imageUrl = imageUrl
)