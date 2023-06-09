package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.openapi.model.AppUserArticleDTO

fun AppUserArticleDTO.asEntity() = Article(
    id = id.asEntity(),
    rating = rating.asEntity(),
    status = status.asEntity(),
    headline = headline,
    lead = lead,
    imageUrl = imageUrl,
    articleBodyHtml = articleBodyHtml,
    isBookmarked = bookmarked
)