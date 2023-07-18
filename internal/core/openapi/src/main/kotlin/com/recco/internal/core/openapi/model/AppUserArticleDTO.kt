/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.recco.internal.core.openapi.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *
 *
 * @param id
 * @param rating
 * @param status
 * @param bookmarked
 * @param headline
 * @param lead
 * @param imageUrl
 * @param articleBodyHtml
 */
@JsonClass(generateAdapter = true)
data class AppUserArticleDTO(

    @Json(name = "id")
    val id: ContentIdDTO,

    @Json(name = "rating")
    val rating: RatingDTO,

    @Json(name = "status")
    val status: StatusDTO,

    @Json(name = "bookmarked")
    val bookmarked: kotlin.Boolean,

    @Json(name = "headline")
    val headline: kotlin.String,

    @Json(name = "lead")
    val lead: kotlin.String? = null,

    @Json(name = "imageUrl")
    val imageUrl: kotlin.String? = null,

    @Json(name = "articleBodyHtml")
    val articleBodyHtml: kotlin.String? = null
)
