package com.shadowflight.network.http

import com.shadowflight.model.PAT
import com.shadowflight.model.SDKConfig
import com.shadowflight.model.UserAuthCredentials
import com.shadowflight.model.didTokenExpired
import com.shadowflight.openapi.api.AuthenticationApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sdkConfig: SDKConfig,
    private val userAuthCredentials: UserAuthCredentials,
    private val authenticationApi: AuthenticationApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val pat = if (userAuthCredentials.pat == null
            || userAuthCredentials.pat!!.didTokenExpired()
        ) {
            getNewPat()
                .run { PAT(accessToken = accessToken, expirationDate = expirationDate) }
                .also { pat -> userAuthCredentials.setPAT(pat) }
        } else {
            userAuthCredentials.pat!!
        }

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${pat.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }

    private fun getNewPat() = runBlocking {
        authenticationApi.login(
            authorization = "Bearer ${sdkConfig.apiSecret}",
            clientUserId = userAuthCredentials.id!!
        ).unwrap()
    }
}