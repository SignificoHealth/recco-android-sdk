package com.shadowflight.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.shadowflight.core.ui.theme.AppTheme
import com.shadowflight.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppNavHost(
                    navController = rememberNavController()
                )
            }
        }
    }
}
