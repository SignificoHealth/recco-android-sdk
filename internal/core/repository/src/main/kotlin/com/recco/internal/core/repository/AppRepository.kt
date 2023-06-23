package com.recco.internal.core.repository

import com.recco.internal.core.base.di.ApplicationScope
import com.recco.internal.core.logger.Logger
import com.recco.api.model.SDKConfig
import com.recco.internal.core.openapi.api.AuthenticationApi
import com.recco.internal.core.openapi.api.MetricApi
import com.recco.internal.core.openapi.model.AppUserMetricActionDTO
import com.recco.internal.core.openapi.model.AppUserMetricCategoryDTO
import com.recco.internal.core.openapi.model.AppUserMetricEventDTO
import com.recco.internal.core.openapi.model.PATReferenceDeleteDTO
import com.recco.internal.core.persistence.AuthCredentials
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
    fun init(sdkConfig: com.recco.api.model.SDKConfig) {
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