package com.recco.core.repository.mapper

import com.recco.core.model.recommendation.Rating
import com.recco.core.openapi.model.RatingDTO

fun RatingDTO.asEntity() = when (this) {
    RatingDTO.LIKE -> Rating.LIKE
    RatingDTO.DISLIKE -> Rating.DISLIKE
    RatingDTO.NOT_RATED -> Rating.NOT_RATED
}

fun Rating.asDTO() = when (this) {
    Rating.LIKE -> RatingDTO.LIKE
    Rating.DISLIKE -> RatingDTO.DISLIKE
    Rating.NOT_RATED -> RatingDTO.NOT_RATED
}