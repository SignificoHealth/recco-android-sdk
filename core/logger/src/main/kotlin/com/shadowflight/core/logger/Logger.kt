package com.shadowflight.core.logger

import android.util.Log
import javax.inject.Inject

class Logger @Inject constructor() {
    fun e(e: Throwable) {
        Log.wtf(null, e)
    }

    fun d(message: String, tag: String? = null) {
        Log.d(tag, message)
    }
}