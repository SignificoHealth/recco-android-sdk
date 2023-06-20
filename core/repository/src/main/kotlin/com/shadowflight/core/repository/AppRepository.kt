package com.shadowflight.core.repository

import com.shadowflight.core.model.SDKConfig
import com.shadowflight.core.openapi.api.AuthenticationApi
import com.shadowflight.core.openapi.api.MetricApi
import com.shadowflight.core.openapi.model.PATReferenceDeleteDTO
import com.shadowflight.core.persistence.AuthCredentials
import com.vilua.core.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val metricApi: MetricApi,
    private val authCredentials: AuthCredentials,
    @ApplicationScope private val appScope: CoroutineScope,
) {
    fun init(sdkConfig: SDKConfig) {
        authCredentials.init(sdkConfig)
    }

    fun loginUser(userId: String) {
        authCredentials.setUserId(userId)
        appScope.launch { metricApi.loginEvent() }
    }

    fun logoutUser() {
        val apiSecret = authCredentials.sdkConfig.apiSecret
        val userId = authCredentials.userId ?: return
        val tokenId = authCredentials.tokenId ?: return

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