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
 * @param accessToken 
 * @param expirationDate 
 * @param tokenId 
 * @param creationDate 
 */
@JsonClass(generateAdapter = true)
data class PATDTO(

    @Json(name = "accessToken")
    val accessToken: kotlin.String,

    @Json(name = "expirationDate")
    val expirationDate: java.time.OffsetDateTime,

    @Json(name = "tokenId")
    val tokenId: kotlin.String,

    @Json(name = "creationDate")
    val creationDate: java.time.OffsetDateTime
)
