package com.recco.internal.core.network.http.interceptors

import com.recco.internal.core.model.exceptions.ApiErrorException
import com.recco.internal.core.model.exceptions.InternalServerErrorException
import com.recco.internal.core.model.exceptions.NoConnectException
import com.recco.internal.core.model.exceptions.ServiceUnavailableException
import com.recco.internal.core.openapi.model.ApiErrorDTO
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import javax.net.ssl.SSLHandshakeException

internal class ErrorInterceptor : Interceptor {

    private val moshi by lazy {
        Moshi.Builder()
            .build()
            .adapter(ApiErrorDTO::class.java)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (e: ConnectException) {
            throw NoConnectException(e.message)
        } catch (e: SSLHandshakeException) {
            throw NoConnectException(e.message)
        }

        if (!response.isSuccessful) {
            val responseBody: String = response.body?.string().orEmpty()
            val apiError = getApiError(responseBody)

            if (apiError != null) {
                throw ApiErrorException(response.getErrorMessage(responseBody, apiError))
            } else {
                when (response.code) {
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                        throw InternalServerErrorException(response.getErrorMessage(responseBody))
                    }

                    HttpURLConnection.HTTP_UNAVAILABLE -> {
                        throw ServiceUnavailableException(response.getErrorMessage(responseBody))
                    }

                    else -> {
                        throw ApiErrorException(response.getErrorMessage(responseBody))
                    }
                }
            }
        }

        return response
    }

    private fun getApiError(responseBody: String): ApiErrorDTO? {
        return try {
            moshi.fromJson(responseBody)
        } catch (exception: java.lang.Exception) {
            null
        }
    }
}

private fun Response.getErrorMessage(responseBody: String, apiError: ApiErrorDTO? = null): String {
    val details = apiError?.let { apiError.string() } ?: responseBody
    return "$code ${request.method} ${request.url} -> $details"
}

private fun ApiErrorDTO?.string(): String {
    return this?.let { "$errorCode($traceId): $message" } ?: ""
}
