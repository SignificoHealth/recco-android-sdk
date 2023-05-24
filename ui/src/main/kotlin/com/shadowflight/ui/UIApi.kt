package com.shadowflight.ui

import android.content.Context
import android.content.Intent

object UIApi {

    fun navigateToFeed(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}