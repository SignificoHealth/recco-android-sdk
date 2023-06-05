package com.shadowflight.core.network.http.interceptors

import com.shadowflight.core.model.authentication.PAT
import com.shadowflight.core.model.authentication.isTokenExpired
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.AuthenticationApi
import com.shadowflight.persistence.AuthCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authCredentials: AuthCredentials,
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
        val pat = authCredentials.pat

        if (pat == null || pat.isTokenExpired()) {
            val userId =
                authCredentials.userId ?: throw IllegalStateException("No userId has been found.")
            authenticationApi.login(
                authorization = "Bearer ${authCredentials.sdkConfig.apiSecret}",
                clientUserId = userId
            ).unwrap()
                .run {
                    PAT(
                        accessToken = accessToken,
                        expirationDate = expirationDate,
                        tokenId = tokenId
                    )
                }.also { authCredentials.setPAT(it) }
        } else {
            pat
        }
    }
}