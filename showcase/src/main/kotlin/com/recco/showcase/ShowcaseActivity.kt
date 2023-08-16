package com.recco.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.recco.api.ui.ReccoApiUI
import com.recco.showcase.navigation.ShowcaseNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowcaseActivity : ComponentActivity() {
    @Inject
    lateinit var persistence: ShowcaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShowcaseNavHost(
                updateClientId = { persistence.setUserId(it) },
                logout = {
                    ReccoApiUI.logout()
                    persistence.clearUserId()
                },
                openReccoClick = {
                    ReccoApiUI.navigateToDashboard(this@ShowcaseActivity)
                },
                navController = rememberNavController(),
                isUserLoggedIn = persistence.isUserLoggedIn()
            )
        }
    }
}
