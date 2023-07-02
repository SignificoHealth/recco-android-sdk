package com.recco.showcase

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ireward.htmlcompose.HtmlText
import com.recco.api.ui.ReccoApiUI
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@OptIn(ExperimentalMaterial3Api::class)
class ShowcaseActivity : ComponentActivity() {
    private val prefs by lazy { getPreferences(Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFEBEBEB))
                    .padding(all = 24.dp),
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

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    painter = painterResource(id = R.drawable.bg_logo),
                    contentDescription = ""
                )

                when (currentScreen.value) {
                    Screen.LoginForm -> LoginForm(currentScreen)
                    Screen.Logout -> Logout(currentScreen)
                }
            }
        }
    }

    @Composable
    private fun ColumnScope.LoginForm(currentScreen: MutableState<Screen>) {
        var textField by remember { mutableStateOf(prefs.getString(USER_ID_KEY, "") ?: "") }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            text = stringResource(id = R.string.login_message),
            style = Typography.bodyMedium,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            text = stringResource(id = R.string.user_id),
            style = Typography.labelSmall,
        )

        Surface(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 2.dp,
        ) {
            TextField(
                value = textField,
                modifier = Modifier.fillMaxWidth(),
                textStyle = Typography.bodyMedium,
                onValueChange = { newText -> textField = newText },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = WarmBrown,
                    disabledTextColor = Color.Transparent,
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = WarmBrown,
                contentColor = SoftYellow,
            ),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                ReccoApiUI.login(userId = textField)
                with(prefs.edit()) {
                    putString(USER_ID_KEY, textField)
                    apply()
                }
                currentScreen.value = Screen.Logout
            }
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = Typography.displayMedium,
            )
        }
    }

    @Composable
    private fun ColumnScope.Logout(currentScreen: MutableState<Screen>) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 42.dp, horizontal = 40.dp),
            painter = painterResource(id = R.drawable.bg_shapes),
            contentDescription = ""
        )

        HtmlText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.wellcome_message),
            style = Typography.bodyLarge.copy(textAlign = TextAlign.Center),
            URLSpanStyle = SpanStyle(
                color = WarmBrown,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.None
            )
        )

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = WarmBrown,
                contentColor = SoftYellow,
            ),
            shape = RoundedCornerShape(0.dp),
            onClick = { ReccoApiUI.navigateToDashboard(this@ShowcaseActivity) }
        ) {
            Text(
                text = stringResource(id = R.string.open_recco),
                style = Typography.displayMedium,
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = WarmBrown,
            ),
            shape = RoundedCornerShape(0.dp),
            border = BorderStroke(width = 1.dp, WarmBrown),
            onClick = {
                ReccoApiUI.logout()
                with(prefs.edit()) {
                    putString(USER_ID_KEY, null)
                    apply()
                }
                currentScreen.value = Screen.LoginForm
            }
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = Typography.displayMedium.copy(color = WarmBrown),
            )
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