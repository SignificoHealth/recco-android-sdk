package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.openapi.model.AppUserArticleDTO

fun AppUserArticleDTO.asEntity() = Article(
    id = id.asEntity(),
    rating = when (rating) {
        AppUserArticleDTO.Rating.LIKE -> Rating.LIKE
        AppUserArticleDTO.Rating.DISLIKE -> Rating.DISLIKE
        AppUserArticleDTO.Rating.NOT_RATED -> Rating.NOT_RATED
    },
    status = when (status) {
        AppUserArticleDTO.Status.NO_INTERACTION -> Status.NO_INTERACTION
        AppUserArticleDTO.Status.VIEWED -> Status.VIEWED
    },
    headline = headline,
    lead = lead,
    imageUrl = imageUrl,
    articleBodyHtml = articleBodyHtml
)