package com.recco.core.questionnaire.numeric

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.recco.core.ui.theme.AppSpacing
import com.recco.core.ui.theme.AppTheme

@Composable
fun NumericTextField(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    onValueChange: (String) -> Unit,
    supportDecimal: Boolean = false,
    maxChars: Int,
    label: String? = null,
    hint: String? = null
) {
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(initialValue)
        )
    }
    var isFocused by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .onFocusChanged { isFocused = it.isFocused },
        value = textFieldValue,
        onValueChange = {
            if ((it.text.toIntOrNull()?.toString()?.length ?: 0) <= maxChars
            ) {
                if (it.text.isEmpty()) {
                    textFieldValue = it
                    onValueChange(it.text)
                } else if (supportDecimal && it.text.toBigDecimalOrNull() != null) {
                    textFieldValue = it
                    onValueChange(it.text)
                } else if (it.text.toIntOrNull() != null) {
                    textFieldValue = it
                    onValueChange(it.text)
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = if (isFocused) {
                AppTheme.colors.accent20
            } else {
                Color.Transparent
            },
            focusedIndicatorColor = AppTheme.colors.accent,
            unfocusedIndicatorColor = AppTheme.colors.primary20
        ),
        textStyle = AppTheme.typography.body2,
        shape = RoundedCornerShape(topEnd = AppSpacing.dp_8, topStart = AppSpacing.dp_8),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        label = if (label != null) {
            {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = label, style = AppTheme.typography.body2.copy(
                            color = AppTheme.colors.primary40
                        )
                    )
                }
            }
        } else {
            null
        },
        placeholder = if (hint != null) {
            {
                Text(
                    text = hint,
                    style = AppTheme.typography.body2.copy(
                        color = AppTheme.colors.primary40
                    )
                )
            }
        } else {
            null
        }
    )
}

@Preview
@Composable
private fun PreviewInteger() {
    NumericTextField(
        maxChars = 5,
        supportDecimal = false,
        initialValue = "",
        onValueChange = { }
    )
}

@Preview
@Composable
private fun PreviewDecimal() {
    NumericTextField(
        maxChars = 5,
        supportDecimal = true,
        initialValue = "",
        onValueChange = {}
    )
}

@Preview
@Composable
private fun PreviewWithLabel() {
    NumericTextField(
        maxChars = 5,
        supportDecimal = false,
        initialValue = "",
        onValueChange = { },
        label = "min."
    )
}

@Preview
@Composable
private fun PreviewWithHint() {
    NumericTextField(
        maxChars = 5,
        supportDecimal = false,
        initialValue = "",
        onValueChange = { },
        hint = "0.00"
    )
}