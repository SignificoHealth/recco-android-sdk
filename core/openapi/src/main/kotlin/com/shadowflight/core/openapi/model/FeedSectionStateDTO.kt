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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * Values: LOCK,UNLOCK,PARTIALLY_UNLOCK
 */
@Suppress("RemoveRedundantQualifierName")
@JsonClass(generateAdapter = false)
enum class FeedSectionStateDTO(val value: kotlin.String) {

    @Json(name = "LOCK")
    LOCK("LOCK"),

    @Json(name = "UNLOCK")
    UNLOCK("UNLOCK"),

    @Json(name = "PARTIALLY_UNLOCK")
    PARTIALLY_UNLOCK("PARTIALLY_UNLOCK");

    /**
     * Override toString() to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value
}