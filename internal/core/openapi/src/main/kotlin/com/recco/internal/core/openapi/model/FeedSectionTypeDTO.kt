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
 * Values: PHYSICAL_ACTIVITY_RECOMMENDATIONS,NUTRITION_RECOMMENDATIONS,PHYSICAL_WELLBEING_RECOMMENDATIONS,SLEEP_RECOMMENDATIONS,PREFERRED_RECOMMENDATIONS,MOST_POPULAR,NEW_CONTENT,PHYSICAL_ACTIVITY_EXPLORE,NUTRITION_EXPLORE,PHYSICAL_WELLBEING_EXPLORE,SLEEP_EXPLORE,STARTING_RECOMMENDATIONS
 */
@Suppress("RemoveRedundantQualifierName")
@JsonClass(generateAdapter = false)
enum class FeedSectionTypeDTO(val value: kotlin.String) {

    @Json(name = "PHYSICAL_ACTIVITY_RECOMMENDATIONS")
    PHYSICAL_ACTIVITY_RECOMMENDATIONS("PHYSICAL_ACTIVITY_RECOMMENDATIONS"),

    @Json(name = "NUTRITION_RECOMMENDATIONS")
    NUTRITION_RECOMMENDATIONS("NUTRITION_RECOMMENDATIONS"),

    @Json(name = "PHYSICAL_WELLBEING_RECOMMENDATIONS")
    PHYSICAL_WELLBEING_RECOMMENDATIONS("PHYSICAL_WELLBEING_RECOMMENDATIONS"),

    @Json(name = "SLEEP_RECOMMENDATIONS")
    SLEEP_RECOMMENDATIONS("SLEEP_RECOMMENDATIONS"),

    @Json(name = "PREFERRED_RECOMMENDATIONS")
    PREFERRED_RECOMMENDATIONS("PREFERRED_RECOMMENDATIONS"),

    @Json(name = "MOST_POPULAR")
    MOST_POPULAR("MOST_POPULAR"),

    @Json(name = "NEW_CONTENT")
    NEW_CONTENT("NEW_CONTENT"),

    @Json(name = "PHYSICAL_ACTIVITY_EXPLORE")
    PHYSICAL_ACTIVITY_EXPLORE("PHYSICAL_ACTIVITY_EXPLORE"),

    @Json(name = "NUTRITION_EXPLORE")
    NUTRITION_EXPLORE("NUTRITION_EXPLORE"),

    @Json(name = "PHYSICAL_WELLBEING_EXPLORE")
    PHYSICAL_WELLBEING_EXPLORE("PHYSICAL_WELLBEING_EXPLORE"),

    @Json(name = "SLEEP_EXPLORE")
    SLEEP_EXPLORE("SLEEP_EXPLORE"),

    @Json(name = "STARTING_RECOMMENDATIONS")
    STARTING_RECOMMENDATIONS("STARTING_RECOMMENDATIONS");

    /**
     * Override toString() to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value
}