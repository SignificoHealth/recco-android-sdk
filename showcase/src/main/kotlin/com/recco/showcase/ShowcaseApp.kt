package com.recco.showcase

import android.app.Application
import android.util.Log
import com.recco.api.model.ReccoLogger
import com.recco.api.model.SDKConfig
import com.recco.api.ui.ReccoApiUI
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowcaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // local: BRBzgGrjAMdvPdXdrIEs1QMtjjKlF3Dfp81sYwWuF1VnTNKUKxQlBvamcU0SqZ4GTAmClvo
        // staging: yvU5m39iXgVtOOKSQqz8neU5mP5HkOamKKMhcX5FDdBE6s6lmrdkC87XQr5dApi5r-vVOFo
        ReccoApiUI.init(
            sdkConfig = SDKConfig(
                appName = "Showcase",
                apiSecret = "yvU5m39iXgVtOOKSQqz8neU5mP5HkOamKKMhcX5FDdBE6s6lmrdkC87XQr5dApi5r-vVOFo",
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
    }
}