package com.recco.core.logger

import android.util.Log
import javax.inject.Inject

class Logger @Inject constructor() {
    fun e(e: Throwable, tag: String? = null, message: String? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, e)
        } else {
            // TODO send logs to Sentry in prod
        }
    }

    fun d(message: String, tag: String? = null) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        } else {
            // TODO send logs to Sentry in prod
        }
    }
}