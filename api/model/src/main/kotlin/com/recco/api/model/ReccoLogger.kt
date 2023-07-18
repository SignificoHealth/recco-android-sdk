package com.recco.api.model

interface ReccoLogger {
    fun e(e: Throwable, tag: String? = null, message: String? = null)
    fun d(message: String, tag: String? = null)
}
