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

import com.shadowflight.core.openapi.model.QuestionDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 
 * @param categoriesIds 
 * @param questions 
 */
@JsonClass(generateAdapter = true)
data class QuestionnaireDTO(

    @Json(name = "id")
    val id: kotlin.String,

    @Json(name = "categoriesIds")
    val categoriesIds: kotlin.collections.List<kotlin.Int>,

    @Json(name = "questions")
    val questions: kotlin.collections.List<QuestionDTO>
)
