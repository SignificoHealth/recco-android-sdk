package com.recco.showcase

import android.app.Application
import android.util.Log
import com.recco.api.model.ReccoConfig
import com.recco.api.model.ReccoLogger
import com.recco.api.model.ReccoPalette
import com.recco.api.ui.ReccoApiUI
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowcaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initSDK(this)
    }

    companion object {
        fun initSDK(application: Application, palette: ReccoPalette = ReccoPalette.Fresh) {
            ReccoApiUI.init(
                sdkConfig = ReccoConfig(
                    appName = "Showcase",
                    clientSecret = BuildConfig.CLIENT_SECRET,
                    palette = palette
                ),
                application = application,
                logger = object : ReccoLogger {
                    override fun e(e: Throwable, tag: String?, message: String?) {
                        Log.e(tag, message, e)
                    }

                    override fun d(message: String, tag: String?) {
                        Log.d(tag, message)
                    }
                }
            )
        }
    }
}
