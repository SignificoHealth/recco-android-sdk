package com.shadowflight.core.network.http

import retrofit2.Response

/**
 * Throws an exception if the api call response contains an error body,
 * otherwise returns the response body.
 */
fun <T> Response<T>.unwrap(): T {
    return body()!!
}