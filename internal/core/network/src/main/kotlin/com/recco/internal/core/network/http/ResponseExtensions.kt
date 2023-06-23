package com.recco.internal.core.network.http

import retrofit2.Response

fun <T> Response<T>.unwrap(): T {
    return body()!!
}