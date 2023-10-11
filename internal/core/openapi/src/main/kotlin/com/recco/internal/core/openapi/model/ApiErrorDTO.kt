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
 * @param errorCode 
 * @param message 
 * @param traceId 
 */
@JsonClass(generateAdapter = true)
data class ApiErrorDTO(

    @Json(name = "errorCode")
    val errorCode: ApiErrorDTO.ErrorCode,

    @Json(name = "message")
    val message: kotlin.String,

    @Json(name = "traceId")
    val traceId: kotlin.String
) {
    /**
     * 
     *
     * Values: UNKNOWN,RESOURCE_NOT_FOUND,UNAUTHORIZED,APP_DISABLED,BAD_REQUEST,CONFLICT,TRANSIENT_TOKEN_ALREADY_CONSUMED
     */
    @JsonClass(generateAdapter = false)
    enum class ErrorCode(val value: kotlin.String) {
        @Json(name = "unknown") UNKNOWN("unknown"),
        @Json(name = "resource_not_found") RESOURCE_NOT_FOUND("resource_not_found"),
        @Json(name = "unauthorized") UNAUTHORIZED("unauthorized"),
        @Json(name = "app_disabled") APP_DISABLED("app_disabled"),
        @Json(name = "bad_request") BAD_REQUEST("bad_request"),
        @Json(name = "conflict") CONFLICT("conflict"),
        @Json(name = "transient_token_already_consumed") TRANSIENT_TOKEN_ALREADY_CONSUMED("transient_token_already_consumed");
    }
}

