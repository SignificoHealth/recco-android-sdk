package com.recco.internal.core.model.recommendation

data class Recommendation(
    val id: ContentId,
    val rating: Rating,
    val status: Status,
    val bookmarked: Boolean,
    val headline: String,
    val lead: String? = null,
    val imageUrl: String? = null
)