package com.recco.core.questionnaire.multichoice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.core.ui.R
import com.recco.core.ui.theme.AppSpacing
import com.recco.core.ui.theme.AppTheme

@Composable
fun MultiChoiceInput(
    modifier: Modifier = Modifier,
    selected: Boolean,
    singleChoice: Boolean = false,
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = onClick,
        shape = RoundedCornerShape(AppSpacing.dp_8),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                AppTheme.colors.accent
            } else {
                AppTheme.colors.primary20
            }
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (selected) {
                AppTheme.colors.accent20
            } else {
                AppTheme.colors.background
            },
        ),
        contentPadding = PaddingValues(vertical = AppSpacing.dp_12, horizontal = AppSpacing.dp_16)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.body2.copy(
                color = if (selected) {
                    AppTheme.colors.onAccent
                } else {
                    AppTheme.colors.primary
                }
            ),
            text = text
        )

        if (!singleChoice) {
            Spacer(modifier = Modifier.width(AppSpacing.dp_8))
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    if (selected) {
                        R.drawable.ic_checkmark
                    } else {
                        R.drawable.ic_add
                    }
                ),
                contentDescription = null,
                tint = if (selected) {
                    AppTheme.colors.accent
                } else {
                    AppTheme.colors.primary
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMultiChoice() {
    var selected by remember { mutableStateOf(false) }
    MultiChoiceInput(
        selected = selected,
        text = "Sales Representative",
        onClick = {
            selected = !selected
        }
    )
}

@Preview
@Composable
private fun PreviewSelectedMultiChoice() {
    var selected by remember { mutableStateOf(false) }

    MultiChoiceInput(
        selected = selected,
        text = "Sales Representative",
        onClick = {
            selected = !selected
        }
    )
}

@Preview
@Composable
private fun PreviewSingleChoice() {
    var selected by remember { mutableStateOf(false) }
    MultiChoiceInput(
        singleChoice = true,
        selected = selected,
        text = "Sales Representative",
        onClick = {
            selected = !selected
        }
    )
}

@Preview
@Composable
private fun PreviewSelectedSingleChoice() {
    var selected by remember { mutableStateOf(false) }

    MultiChoiceInput(
        singleChoice = true,
        selected = selected,
        text = "Sales Representative",
        onClick = {
            selected = !selected
        }
    )
}


@Preview
@Composable
private fun PreviewLargeText() {
    var selected by remember { mutableStateOf(false) }
    MultiChoiceInput(
        selected = selected,
        text = "I've already done something, but I'm not doing anything now.I'm doing something about it now and I intend to do something in the future.",
        onClick = {
            selected = !selected
        }
    )
}
