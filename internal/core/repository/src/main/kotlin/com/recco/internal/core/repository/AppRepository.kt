package com.recco.internal.core.repository

import com.recco.api.model.ReccoConfig
import com.recco.internal.core.base.di.ApplicationScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.metric.AppUserMetricAction
import com.recco.internal.core.model.metric.AppUserMetricCategory
import com.recco.internal.core.model.metric.AppUserMetricEvent
import com.recco.internal.core.openapi.api.AuthenticationApi
import com.recco.internal.core.openapi.api.MetricApi
import com.recco.internal.core.openapi.model.PATReferenceDeleteDTO
import com.recco.internal.core.persistence.AuthCredentials
import com.recco.internal.core.repository.mapper.asDTO
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
        logEvent(
            AppUserMetricEvent(
                category = AppUserMetricCategory.USER_SESSION,
                action = AppUserMetricAction.LOGIN
            )
        )
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

    fun logEvent(event: AppUserMetricEvent) {
        authCredentials.userId?.let {
            appScope.launch {
                runCatching { metricApi.logEvent(event.asDTO()) }
                    .onFailure { logger.e(it) }
            }
        }
    }
}
