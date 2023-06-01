package com.shadowflight.network.http.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class AddHeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Accept-Language", Locale.getDefault().toLanguageTag())
            .build()
        return chain.proceed(newRequest)
    }
}
