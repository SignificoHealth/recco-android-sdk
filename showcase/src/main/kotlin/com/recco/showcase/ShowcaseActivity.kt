package com.recco.showcase

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.recco.api.ui.ReccoApiUI
import com.recco.showcase.navigation.ShowcaseNavHost

class ShowcaseActivity : ComponentActivity() {
    private val prefs by lazy { getPreferences(Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShowcaseNavHost(
                updateClientId = {
                    with(prefs.edit()) {
                        putString(USER_ID_KEY, it)
                        apply()
                    }
                },
                logout = {
                    ReccoApiUI.logout()
                    with(prefs.edit()) {
                        putString(USER_ID_KEY, null)
                        apply()
                    }
                },
                openReccoClick = {
                    ReccoApiUI.navigateToDashboard(this@ShowcaseActivity)
                },
                navController = rememberNavController(),
                isUserLoggedIn = !prefs.getString(USER_ID_KEY, "").isNullOrBlank()
            )
        }
    }

    companion object {
        private const val USER_ID_KEY = "user_id_key"
    }
}