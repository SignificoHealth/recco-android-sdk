package com.shadowflight.ui

import android.content.Context
import android.content.Intent
import com.shadowflight.model.SDKConfig
import com.shadowflight.model.di.ModelModule

object UIApi {
    fun init(sdkConfig: SDKConfig) {
        ModelModule.setUpExternalDependencies(sdkConfig)
    }

    fun login(userId: String) {
        ModelModule.setUserId(userId)
    }

    fun navigateToFeed(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}