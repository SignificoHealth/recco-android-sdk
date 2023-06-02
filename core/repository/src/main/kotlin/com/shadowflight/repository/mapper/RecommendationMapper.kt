package com.shadowflight.repository.mapper

import com.shadowflight.model.recommendation.ContentId
import com.shadowflight.model.recommendation.Rating
import com.shadowflight.model.recommendation.Recommendation
import com.shadowflight.model.recommendation.Status
import com.shadowflight.openapi.model.AppUserRecommendationDTO
import com.shadowflight.openapi.model.ContentIdDTO

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

private fun ContentIdDTO.asEntity() = ContentId(itemId = itemId, catalogId = catalogId)