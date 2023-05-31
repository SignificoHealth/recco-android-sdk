package com.shadowflight.network.http

import com.shadowflight.model.SDKConfig
import com.shadowflight.model.authentication.PAT
import com.shadowflight.model.authentication.UserAuthCredentials
import com.shadowflight.model.authentication.didTokenExpired
import com.shadowflight.openapi.api.AuthenticationApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sdkConfig: SDKConfig,
    private val userAuthCredentials: UserAuthCredentials,
    private val authenticationApi: AuthenticationApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val pat = getPat()

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${pat.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }

    @Synchronized
    private fun getPat() = runBlocking(Dispatchers.IO) {
        if (userAuthCredentials.pat == null
            || userAuthCredentials.pat!!.didTokenExpired()
        ) {
            authenticationApi.login(
                authorization = "Bearer ${sdkConfig.apiSecret}",
                clientUserId = userAuthCredentials.id!!
            ).unwrap()
                .run { PAT(accessToken = accessToken, expirationDate = expirationDate) }
                .also { pat -> userAuthCredentials.setPAT(pat) }
        } else {
            userAuthCredentials.pat!!
        }
    }
}