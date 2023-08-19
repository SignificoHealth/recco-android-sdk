package com.recco.showcase.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.showcase.R
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun ShowcaseTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(label),
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
                value = value,
                modifier = Modifier.fillMaxWidth(),
                textStyle = Typography.bodyMedium,
                onValueChange = { newText -> onValueChange(newText) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = WarmBrown,
                    disabledTextColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ShowcaseTextField(label = R.string.login, value = "admin", onValueChange = {})
}
