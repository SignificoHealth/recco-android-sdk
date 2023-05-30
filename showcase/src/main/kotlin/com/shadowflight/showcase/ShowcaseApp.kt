package com.shadowflight.showcase

import android.app.Application
import com.shadowflight.model.SDKConfig
import com.shadowflight.ui.UIApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowcaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // local: APEb8gSTiH6hoKgmxBx8-kGh27X3pvYXK-Fhn6HO9k7D4WYkMmntLsaPl9bwAlZYCmd8oJw
        // staging: YuJi02IHzJDxe-oiqT1QOptnh9mGMnulPPx5C3xoyBSe0dNha-m1qOjG9DopeSspqR9d6-Y
        UIApi.init(
            sdkConfig = SDKConfig(
                apiSecret = "APEb8gSTiH6hoKgmxBx8-kGh27X3pvYXK-Fhn6HO9k7D4WYkMmntLsaPl9bwAlZYCmd8oJw",
                isDebug = true
            )
        )
    }
}