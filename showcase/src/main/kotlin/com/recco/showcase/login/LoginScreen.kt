package com.recco.showcase.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll   
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.api.ui.ReccoApiUI
import com.recco.showcase.R
import com.recco.showcase.ui.components.ShowcaseButton
import com.recco.showcase.ui.components.ShowcaseTextField
import com.recco.showcase.ui.theme.BackgroundColor
import com.recco.showcase.ui.theme.Typography

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
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            text = stringResource(id = R.string.login_message),
            style = Typography.bodyMedium
        )

        ShowcaseTextField(
            modifier = Modifier.padding(top = 32.dp),
            label = R.string.user_id,
            value = textField,
            onValueChange = { newText -> textField = newText }
        )

        Spacer(Modifier.weight(1f))

        ShowcaseButton(
            onClick = {
                ReccoApiUI.login(userId = textField)
                updateClientId(textField)
                loginClick()
            },
            text = stringResource(R.string.login)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    LoginScreen(updateClientId = {}, loginClick = {})
}
