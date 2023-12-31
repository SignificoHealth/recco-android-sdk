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
 * @param maxValue 
 * @param minValue 
 * @param format 
 */
@JsonClass(generateAdapter = true)
data class NumericQuestionDTO(

    @Json(name = "maxValue")
    val maxValue: kotlin.Int,

    @Json(name = "minValue")
    val minValue: kotlin.Int,

    @Json(name = "format")
    val format: NumericQuestionDTO.Format
) {
    /**
     * 
     *
     * Values: HUMAN_HEIGHT,HUMAN_WEIGHT,INTEGER,DECIMAL
     */
    @JsonClass(generateAdapter = false)
    enum class Format(val value: kotlin.String) {
        @Json(name = "human_height") HUMAN_HEIGHT("human_height"),
        @Json(name = "human_weight") HUMAN_WEIGHT("human_weight"),
        @Json(name = "integer") INTEGER("integer"),
        @Json(name = "decimal") DECIMAL("decimal");
    }
}

