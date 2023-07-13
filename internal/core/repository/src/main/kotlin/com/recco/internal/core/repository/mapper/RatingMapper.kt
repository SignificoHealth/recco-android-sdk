package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.openapi.model.RatingDTO

internal fun RatingDTO.asEntity() = when (this) {
    RatingDTO.LIKE -> Rating.LIKE
    RatingDTO.DISLIKE -> Rating.DISLIKE
    RatingDTO.NOT_RATED -> Rating.NOT_RATED
}

internal fun Rating.asDTO() = when (this) {
    Rating.LIKE -> RatingDTO.LIKE
    Rating.DISLIKE -> RatingDTO.DISLIKE
    Rating.NOT_RATED -> RatingDTO.NOT_RATED
}