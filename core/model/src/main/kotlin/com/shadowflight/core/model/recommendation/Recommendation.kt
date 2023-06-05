package com.shadowflight.core.model.recommendation

data class Recommendation(
    val id: ContentId,
    val rating: Rating,
    val status: Status,
    val headline: String,
    val lead: String? = null,
    val imageUrl: String? = null
)