package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.openapi.model.AppUserArticleDTO

internal fun AppUserArticleDTO.asEntity() = Article(
    id = id.asEntity(),
    rating = rating.asEntity(),
    status = status.asEntity(),
    headline = headline,
    lead = lead,
    audioUrl = audioUrl,
    readingTimeInSeconds = duration,
    imageUrl = dynamicImageResizingUrl,
    imageAlt = imageAlt,
    articleBodyHtml = articleBodyHtml,
    isBookmarked = bookmarked
)
