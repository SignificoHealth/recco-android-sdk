package com.recco.internal.core.network.http.interceptors

import com.recco.internal.core.base.di.IoDispatcher
import com.recco.internal.core.model.authentication.PAT
import com.recco.internal.core.model.authentication.isTokenExpired
import com.recco.internal.core.network.http.unwrap
import com.recco.internal.core.openapi.api.AuthenticationApi
import com.recco.internal.core.persistence.AuthCredentials
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor(
    private val authCredentials: AuthCredentials,
    private val authenticationApi: AuthenticationApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val pat = getPat()

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${pat.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }

    @Synchronized
    private fun getPat() = runBlocking(dispatcher) {
        val pat = authCredentials.pat

        if (pat == null || pat.isTokenExpired()) {
            val userId =
                authCredentials.userId ?: throw IllegalStateException("No userId has been found.")
            authenticationApi.login(
                authorization = "Bearer ${authCredentials.sdkConfig.clientSecret}",
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
