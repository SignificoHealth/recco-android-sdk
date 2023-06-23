package com.recco.api.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.core.ui.components.AppCustomLoading
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.theme.AppTheme
import com.recco.api.ui.navigation.AppNavHost
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
                    Scaffold(
                        topBar = {
                            if (uiState.error != null) {
                                AppTopBar()
                            }
                        },
                        backgroundColor = AppTheme.colors.background,
                        contentPadding = WindowInsets.navigationBars.asPaddingValues()
                    ) { innerPadding ->
                        AppScreenStateAware(
                            contentPadding = innerPadding,
                            uiState = uiState,
                            retry = { viewModel.onUserInteract(MainUserInteract.Retry) },
                            isEmpty = true,
                            emptyContent = {
                                AppCustomLoading(modifier = Modifier.weight(1f))
                            }
                        ) {}
                    }
                }
            }
        }
    }
}
