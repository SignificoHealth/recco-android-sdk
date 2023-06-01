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
import com.shadowflight.openapi.model.AppUserArticleDTO
import com.shadowflight.openapi.model.AppUserRecommendationDTO
import com.shadowflight.openapi.model.TopicDTO
import com.shadowflight.openapi.model.UpdateBookmarkDTO
import com.shadowflight.openapi.model.UpdateRatingDTO
import com.shadowflight.openapi.model.UpdateStatusDTO

interface RecommendationApi {

    /**
     * A list of content filtered by topic.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @param topic 
     * @return [kotlin.collections.List<AppUserRecommendationDTO>]
     */
    @GET("api/v1/me/recommendations/explore/topics/{topic}")
    suspend fun exploreContentByTopic(@Path("topic") topic: TopicDTO): Response<kotlin.collections.List<AppUserRecommendationDTO>>

    /**
     * Get article.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @param recommendationId 
     * @return [AppUserArticleDTO]
     */
    @GET("api/v1/me/recommendations/articles/{recommendationId}")
    suspend fun getArticle(@Path("recommendationId") recommendationId: kotlin.String): Response<AppUserArticleDTO>

    /**
     * A list of most popular content.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @return [kotlin.collections.List<AppUserRecommendationDTO>]
     */
    @GET("api/v1/me/recommendations/most_popular")
    suspend fun getMostPopularContent(): Response<kotlin.collections.List<AppUserRecommendationDTO>>

    /**
     * A list of newest content.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @return [kotlin.collections.List<AppUserRecommendationDTO>]
     */
    @GET("api/v1/me/recommendations/newest")
    suspend fun getNewestContent(): Response<kotlin.collections.List<AppUserRecommendationDTO>>

    /**
     * A list of starting recommendations.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @return [kotlin.collections.List<AppUserRecommendationDTO>]
     */
    @GET("api/v1/me/recommendations/starting")
    suspend fun getStartingRecommendations(): Response<kotlin.collections.List<AppUserRecommendationDTO>>

    /**
     * A list of tailored recommendations filtered by topic.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @param topic 
     * @return [kotlin.collections.List<AppUserRecommendationDTO>]
     */
    @GET("api/v1/me/recommendations/tailored/topics/{topic}")
    suspend fun getTailoredRecommendationsByTopic(@Path("topic") topic: TopicDTO): Response<kotlin.collections.List<AppUserRecommendationDTO>>

    /**
     * A list of recommendations which are improved by the preferences.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 200: OK
     *
     * @return [kotlin.collections.List<AppUserRecommendationDTO>]
     */
    @GET("api/v1/me/recommendations/preferred")
    suspend fun getUserPreferredRecommendations(): Response<kotlin.collections.List<AppUserRecommendationDTO>>

    /**
     * Set recommendation bookmark state.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 204: No Content
     *
     * @param updateBookmarkDTO 
     * @return [Unit]
     */
    @PUT("api/v1/me/recommendations/bookmark")
    suspend fun setBookmark(@Body updateBookmarkDTO: UpdateBookmarkDTO): Response<Unit>

    /**
     * Set recommendation rating.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 204: No Content
     *
     * @param updateRatingDTO 
     * @return [Unit]
     */
    @PUT("api/v1/me/recommendations/rating")
    suspend fun setRating(@Body updateRatingDTO: UpdateRatingDTO): Response<Unit>

    /**
     * Set recommendation status.
     * 
     * Responses:
     *  - 401: Unauthorized
     *  - 204: No Content
     *
     * @param updateStatusDTO 
     * @return [Unit]
     */
    @PUT("api/v1/me/recommendations/status")
    suspend fun setStatus(@Body updateStatusDTO: UpdateStatusDTO): Response<Unit>
}
