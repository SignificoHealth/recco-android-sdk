package com.recco.showcase

import android.app.Application
import android.util.Log
import com.recco.api.model.ReccoConfig
import com.recco.api.model.ReccoLogger
import com.recco.api.model.ReccoStyle
import com.recco.api.ui.ReccoApiUI
import com.recco.showcase.data.ShowcaseRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ShowcaseApp : Application() {
    @Inject
    lateinit var persistence: ShowcaseRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch {
            initSDK(
                this@ShowcaseApp,
                ReccoStyle(
                    font = persistence.getSelectedReccoFont(),
                    palette = persistence.getSelectedReccoPalette()
                )
            )
        }
    }

    companion object {
        fun initSDK(
            application: Application,
            style: ReccoStyle
        ) {
            ReccoApiUI.init(
                sdkConfig = ReccoConfig(
                    clientSecret = BuildConfig.CLIENT_SECRET,
                    style = style
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
