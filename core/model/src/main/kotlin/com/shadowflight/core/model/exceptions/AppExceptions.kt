package com.shadowflight.core.model.exceptions

import java.io.IOException
import java.net.HttpURLConnection

/**
 * IMPORTANT:
 * Ref: https://stackoverflow.com/a/58711127/5502014
 * You should subclass IOException and use that to send information from your
 * interceptors to your calling code.
 * OkHttp considers other exceptions like IllegalStateException to be application crashes.
 */
sealed class AppException(val code: Int, var msg: String) : IOException("($code) $msg")

class ApiErrorException(val description: String? = null) : AppException(
    code = -1,
    msg = description ?: "Api Error"
)

class NoConnectException(val description: String? = null) : AppException(
    code = -2,
    msg = description ?: "Failed to connect to server"
)

class InternalServerErrorException(val description: String? = null) : AppException(
    code = HttpURLConnection.HTTP_INTERNAL_ERROR,
    msg = description ?: "Internal Server Error"
)

class ServiceUnavailableException(val description: String? = null) : AppException(
    code = HttpURLConnection.HTTP_UNAVAILABLE,
    msg = description ?: "Service Unavailable"
)
