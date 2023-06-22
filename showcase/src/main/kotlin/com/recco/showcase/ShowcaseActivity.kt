package com.recco.showcase

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.recco.ui.ReccoApiUI

@OptIn(ExperimentalMaterial3Api::class)
class ShowcaseActivity : ComponentActivity() {
    private val prefs by lazy { getPreferences(Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp)
            ) {
                val currentScreen = remember {
                    mutableStateOf(
                        if (prefs.getString(USER_ID_KEY, "").isNullOrBlank()) {
                            Screen.LoginForm
                        } else {
                            Screen.Logout
                        }
                    )
                }

                when (currentScreen.value) {
                    Screen.LoginForm -> LoginForm(currentScreen)
                    Screen.Logout -> Logout(currentScreen)
                }
            }
        }
    }

    @Composable
    private fun LoginForm(currentScreen: MutableState<Screen>) {
        var textField by remember { mutableStateOf(prefs.getString(USER_ID_KEY, "") ?: "") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textField,
            onValueChange = { newText -> textField = newText },
            label = { Text(text = "User id") },
        )
        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                ReccoApiUI.login(userId = textField)
                with(prefs.edit()) {
                    putString(USER_ID_KEY, textField)
                    apply()
                }
                currentScreen.value = Screen.Logout
            }) {
            Text(text = "Login")
        }
    }

    @Composable
    private fun ColumnScope.Logout(currentScreen: MutableState<Screen>) {
        Text(text = "Current userId ${prefs.getString(USER_ID_KEY, "")}")
        Spacer(Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { ReccoApiUI.navigateToDashboard(this@ShowcaseActivity) }) {
            Text(text = "Open SDK")
        }

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                ReccoApiUI.logout()
                with(prefs.edit()) {
                    putString(USER_ID_KEY, null)
                    apply()
                }
                currentScreen.value = Screen.LoginForm
            }) {
            Text(text = "Logout")
        }
    }

    companion object {
        const val USER_ID_KEY = "user_id_key"
    }
}

sealed class Screen {
    object LoginForm : Screen()
    object Logout : Screen()
}