package com.shadowflight.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.shadowflight.ui.UIApi

class ShowcaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UIApi.login(userId = "1")
        UIApi.navigateToFeed(this)
    }
}