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

package com.shadowflight.openapi.api

import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json
import com.shadowflight.openapi.model.ApiErrorDTO
import com.shadowflight.openapi.model.AppUserDTO

interface AppUserApi {

    /**
     * Return app user.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @return [AppUserDTO]
     */
    @GET("api/v1/me")
    suspend fun get(): Response<AppUserDTO>
}
