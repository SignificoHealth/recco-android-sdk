package com.shadowflight.showcase

import android.app.Application
import com.shadowflight.model.SDKConfig
import com.shadowflight.ui.UIApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowcaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        UIApi.init(
            sdkConfig = SDKConfig(
                apiSecret = "zNtOVYGN_w37XHfM1erpxosbbCQmjgUXqHICO5vEBsI4M90A5ERQLeRNQKNexkVSmAtEqhY",
                isDebug = true
            )
        )
    }
}