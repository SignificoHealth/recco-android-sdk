package com.shadowflight.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.shadowflight.core.ui.components.AppScreenStateAware
import com.shadowflight.core.ui.theme.AppTheme
import com.shadowflight.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.viewState.collectAsStateWithLifecycle()
            AppTheme {
                val user = uiState.data?.user
                if (user != null) {
                    AppNavHost(
                        user = user,
                        navController = rememberNavController()
                    )

                } else {
                    AppScreenStateAware(
                        uiState = uiState,
                        retry = { viewModel.onUserInteract(MainUserInteract.Retry) },
                        content = {}
                    )
                }
            }
        }
    }
}
