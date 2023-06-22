package com.recco.core.repository.mapper

import com.recco.core.model.recommendation.Article
import com.recco.core.openapi.model.AppUserArticleDTO

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