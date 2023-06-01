package com.shadowflight.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.shadowflight.ui.navigation.AppNavHost
import com.shadowflight.uicommons.components.AppStatusBar
import com.shadowflight.uicommons.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppStatusBar()
                AppNavHost(
                    navController = rememberNavController()
                )
            }
        }
    }
}