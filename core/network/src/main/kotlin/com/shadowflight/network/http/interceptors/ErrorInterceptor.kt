package com.shadowflight.network.http.interceptors

import com.shadowflight.model.exceptions.InternalServerErrorException
import com.shadowflight.model.exceptions.NoConnectException
import com.shadowflight.model.exceptions.NotFoundException
import com.shadowflight.model.exceptions.ServiceUnavailableException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import javax.net.ssl.SSLHandshakeException

class ErrorInterceptor : Interceptor {

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

        when (response.code) {
            HttpURLConnection.HTTP_NOT_FOUND -> {
                throw NotFoundException(response.getErrorMessage())
            }

            HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                throw InternalServerErrorException(response.getErrorMessage())
            }

            HttpURLConnection.HTTP_UNAVAILABLE -> {
                throw ServiceUnavailableException(response.getErrorMessage())
            }
        }

        return response
    }
}

private fun Response.getErrorMessage(): String {
    return this.request.method + " " + this.request.url.toString() + " -> " + this.body?.string()
}
