package com.recco.showcase

import android.app.Application
import com.recco.core.model.SDKConfig
import com.recco.ui.ReccoApiUI
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowcaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // local: IJl9F1P8yUTqqcHnlZNYw2IZ_5UKJOtCRcPrw78POZ4ouFyFkZV0HzRGE2qYMh2V17O2t34
        // staging: YuJi02IHzJDxe-oiqT1QOptnh9mGMnulPPx5C3xoyBSe0dNha-m1qOjG9DopeSspqR9d6-Y
        ReccoApiUI.init(
            sdkConfig = SDKConfig(
                appName = "Showcase",
                apiSecret = "YuJi02IHzJDxe-oiqT1QOptnh9mGMnulPPx5C3xoyBSe0dNha-m1qOjG9DopeSspqR9d6-Y"
            ),
            application = this
        )
    }
}