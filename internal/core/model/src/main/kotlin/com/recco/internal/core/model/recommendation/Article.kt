package com.recco.internal.core.model.recommendation

data class Article(
    val id: ContentId,
    val rating: Rating,
    val status: Status,
    val isBookmarked: Boolean,
    val headline: String,
    val lead: String? = null,
    val imageUrl: String? = null,
    val articleBodyHtml: String? = null
)
