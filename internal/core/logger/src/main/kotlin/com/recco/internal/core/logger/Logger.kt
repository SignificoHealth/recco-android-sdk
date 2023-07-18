package com.recco.internal.core.logger

import com.recco.api.model.ReccoLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger @Inject constructor() {
    private var clientLogger: ReccoLogger? = null

    fun setupClientLogger(logger: ReccoLogger?) {
        this.clientLogger = logger
    }

    fun e(e: Throwable, tag: String? = null, message: String? = null) {
        clientLogger?.e(e, tag, message)
    }

    fun d(message: String, tag: String? = null) {
        if (BuildConfig.DEBUG) {
            clientLogger?.d(message, tag)
        }
    }
}
