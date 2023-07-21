package com.recco.showcase

import android.app.Application
import android.util.Log
import com.recco.api.model.ReccoConfig
import com.recco.api.model.ReccoLogger
import com.recco.api.ui.ReccoApiUI
import com.recco.internal.core.network.di.NetworkModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowcaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        ReccoApiUI.init(
            sdkConfig = ReccoConfig(
                appName = "Showcase",
                apiSecret = BuildConfig.CLIENT_SECRET
                /*                palette = ReccoPalette.Custom(
                                    lightColors = ReccoPalette.Default.lightColors.copy(primary = Color(0xFFFFEB3B)),
                                    darkColors = ReccoPalette.Default.darkColors
                                )*/
            ),
            application = this,
            logger = object : ReccoLogger {
                override fun e(e: Throwable, tag: String?, message: String?) {
                    Log.e(tag, message, e)
                }

                override fun d(message: String, tag: String?) {
                    Log.d(tag, message)
                }
            }
        )

        // This is only for internal usage: please, do not change Recco base url.
        if (BuildConfig.DEBUG) {
            NetworkModule.BASE_URL = "https://api.sf-dev.significo.dev/"
        }
    }
}
