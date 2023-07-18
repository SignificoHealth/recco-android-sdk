package com.recco.internal.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.recco.internal.core.ui.R

@Composable
fun AppSecondaryButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int? = null,
    @DrawableRes iconStartRes: Int? = null,
    @DrawableRes iconEndRes: Int? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    AppButton(
        modifier = modifier,
        isPrimary = false,
        text = textRes?.let { stringResource(id = textRes) },
        iconStartRes = iconStartRes,
        iconEndRes = iconEndRes,
        isLoading = isLoading,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun AppSecondaryButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes iconStartRes: Int? = null,
    @DrawableRes iconEndRes: Int? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    AppButton(
        modifier = modifier,
        isPrimary = false,
        text = text,
        iconStartRes = iconStartRes,
        iconEndRes = iconEndRes,
        isLoading = isLoading,
        enabled = enabled,
        onClick = onClick
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonActiveOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Button",
        iconStartRes = null,
        iconEndRes = null,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonInactiveOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Button",
        iconStartRes = null,
        iconEndRes = null,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonActiveWithEndIconOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.recco_ic_retry,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonInactiveWithEndIconOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.recco_ic_retry,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonActiveWithStartIconOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Button",
        iconStartRes = R.drawable.recco_ic_retry,
        iconEndRes = null,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonInactiveWithStartIconOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Button",
        iconStartRes = R.drawable.recco_ic_retry,
        iconEndRes = null,
        enabled = false,
        onClick = {}
    )
}
