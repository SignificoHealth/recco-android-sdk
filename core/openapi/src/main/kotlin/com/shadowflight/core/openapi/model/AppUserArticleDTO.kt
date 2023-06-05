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

package com.shadowflight.core.openapi.model

import com.shadowflight.core.openapi.model.ContentIdDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 
 * @param rating 
 * @param status 
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
    val rating: AppUserArticleDTO.Rating,

    @Json(name = "status")
    val status: AppUserArticleDTO.Status,

    @Json(name = "headline")
    val headline: kotlin.String,

    @Json(name = "lead")
    val lead: kotlin.String? = null,

    @Json(name = "imageUrl")
    val imageUrl: kotlin.String? = null,

    @Json(name = "articleBodyHtml")
    val articleBodyHtml: kotlin.String? = null
) {
    /**
     * 
     *
     * Values: LIKE,DISLIKE,NOT_RATED
     */
    @JsonClass(generateAdapter = false)
    enum class Rating(val value: kotlin.String) {
        @Json(name = "like") LIKE("like"),
        @Json(name = "dislike") DISLIKE("dislike"),
        @Json(name = "not_rated") NOT_RATED("not_rated");
    }
    /**
     * 
     *
     * Values: NO_INTERACTION,VIEWED
     */
    @JsonClass(generateAdapter = false)
    enum class Status(val value: kotlin.String) {
        @Json(name = "no_interaction") NO_INTERACTION("no_interaction"),
        @Json(name = "viewed") VIEWED("viewed");
    }
}

