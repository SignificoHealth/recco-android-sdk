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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.recco.api.ui.navigation.AppNavHost
import com.recco.internal.core.ui.components.AppCustomLoading
import com.recco.internal.core.ui.components.AppScreenStateAware
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.GlobalToastEvent
import com.recco.internal.core.ui.pipelines.GlobalViewEvent
import com.recco.internal.core.ui.pipelines.globalViewEvents
import com.recco.internal.core.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.viewState.collectAsStateWithLifecycle()
            val viewEvents = globalViewEvents.collectAsStateWithLifecycle(GlobalViewEvent.NoOp)

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

            when (val event = viewEvents.value) {
                is GlobalViewEvent.ShowToast -> {
                    GlobalToastEvent(
                        type = event.type,
                        title = stringResource(id = event.titleRes),
                        description = event.subtitleRes?.let {
                            stringResource(id = it)
                        }
                    )
                }
            }
        }
    }
}
