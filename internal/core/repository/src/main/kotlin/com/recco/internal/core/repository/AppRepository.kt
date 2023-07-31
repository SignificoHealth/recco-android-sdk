package com.recco.internal.core.repository

import com.recco.api.model.ReccoConfig
import com.recco.internal.core.base.di.ApplicationScope
import com.recco.internal.core.logger.Logger
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
    @ApplicationScope private val appScope: CoroutineScope
) {
    private lateinit var sdkConfig: ReccoConfig

    fun init(sdkConfig: ReccoConfig) {
        this.sdkConfig = sdkConfig
        authCredentials.init(sdkConfig)
    }

    fun getSDKConfig() = sdkConfig

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
        val clientSecret = authCredentials.sdkConfig.clientSecret
        val userId = authCredentials.userId ?: return authCredentials.clearCache()
        val tokenId = authCredentials.tokenId ?: return authCredentials.clearCache()

        appScope.launch {
            runCatching {
                authCredentials.clearCache()
                authenticationApi.logout(
                    authorization = "Bearer $clientSecret",
                    clientUserId = userId,
                    paTReferenceDeleteDTO = PATReferenceDeleteDTO(
                        tokenId = tokenId
                    )
                )
            }.onFailure { logger.e(it) }
        }
    }
}
