package com.recco.showcase.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.api.ui.ReccoApiUI
import com.recco.showcase.R
import com.recco.showcase.ui.theme.BackgroundColor
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    updateClientId: (String) -> Unit,
    loginClick: () -> Unit
) {
    var textField by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)
            .padding(all = 24.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            painter = painterResource(id = R.drawable.bg_logo),
            contentDescription = ""
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            text = stringResource(id = R.string.login_message),
            style = Typography.bodyMedium
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            text = stringResource(id = R.string.user_id),
            style = Typography.labelSmall
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 2.dp
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
                contentColor = SoftYellow
            ),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                ReccoApiUI.login(userId = textField)
                updateClientId(textField)
                loginClick()
            }
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = Typography.displayMedium
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    LoginScreen(updateClientId = {}, loginClick = {})
}
