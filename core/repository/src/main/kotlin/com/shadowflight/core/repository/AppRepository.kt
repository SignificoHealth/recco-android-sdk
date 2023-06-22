package com.shadowflight.core.repository

import com.shadowflight.core.base.di.ApplicationScope
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.model.SDKConfig
import com.shadowflight.core.openapi.api.AuthenticationApi
import com.shadowflight.core.openapi.api.MetricApi
import com.shadowflight.core.openapi.model.AppUserMetricActionDTO
import com.shadowflight.core.openapi.model.AppUserMetricCategoryDTO
import com.shadowflight.core.openapi.model.AppUserMetricEventDTO
import com.shadowflight.core.openapi.model.PATReferenceDeleteDTO
import com.shadowflight.core.persistence.AuthCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val metricApi: MetricApi,
    private val authCredentials: AuthCredentials,
    private val logger: Logger,
    @ApplicationScope private val appScope: CoroutineScope,
) {
    fun init(sdkConfig: SDKConfig) {
        authCredentials.init(sdkConfig)
    }

    fun loginUser(userId: String) {
        authCredentials.setUserId(userId)
        appScope.launch {
            runCatching {
                metricApi.logEvent(
                    AppUserMetricEventDTO(
                        category = AppUserMetricCategoryDTO.USER_SESSION,
                        action = AppUserMetricActionDTO.LOGIN
                    )
                )
            }.onFailure { logger.e(it) }
        }
    }

    fun logoutUser() {
        val apiSecret = authCredentials.sdkConfig.apiSecret
        val userId = authCredentials.userId ?: return authCredentials.clearCache()
        val tokenId = authCredentials.tokenId ?: return authCredentials.clearCache()

        appScope.launch {
            authCredentials.clearCache()
            authenticationApi.logout(
                authorization = "Bearer $apiSecret",
                clientUserId = userId,
                paTReferenceDeleteDTO = PATReferenceDeleteDTO(
                    tokenId = tokenId
                )
            )
        }
    }
}