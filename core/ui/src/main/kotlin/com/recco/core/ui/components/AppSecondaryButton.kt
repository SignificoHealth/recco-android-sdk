package com.recco.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.recco.core.ui.R

@Composable
fun AppSecondaryButton(
    modifier: Modifier = Modifier,
    isOverBackground: Boolean = false,
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
        isOverBackground = isOverBackground,
        text = textRes?.let { stringResource(id = textRes) },
        iconStartRes = iconStartRes,
        iconEndRes = iconEndRes,
        isLoading = isLoading,
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
fun AppSecondaryButton(
    modifier: Modifier = Modifier,
    isOverBackground: Boolean = false,
    text: String? = null,
    @DrawableRes iconStartRes: Int? = null,
    @DrawableRes iconEndRes: Int? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    AppButton(
        modifier = modifier,
        isPrimary = false,
        isOverBackground = isOverBackground,
        text = text,
        iconStartRes = iconStartRes,
        iconEndRes = iconEndRes,
        isLoading = isLoading,
        enabled = enabled,
        onClick = onClick,
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonActiveOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = false,
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
        isOverBackground = false,
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
        isOverBackground = false,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.ic_retry,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonInactiveWithEndIconOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = false,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.ic_retry,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppSecondaryButtonActiveWithStartIconOverLightBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = false,
        text = "Button",
        iconStartRes = R.drawable.ic_retry,
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
        isOverBackground = false,
        text = "Button",
        iconStartRes = R.drawable.ic_retry,
        iconEndRes = null,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppSecondaryButtonActiveOverDarkBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = null,
        iconEndRes = null,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppSecondaryButtonInactiveOverDarkBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = null,
        iconEndRes = null,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppSecondaryButtonActiveWithEndIconOverDarkBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.ic_retry,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppSecondaryButtonInactiveWithEndIconOverDarkBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.ic_retry,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppSecondaryButtonActiveWithStartIconOverDarkBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = R.drawable.ic_retry,
        iconEndRes = null,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppSecondaryButtonInactiveWithStartIconOverDarkBackgroundPreview() {
    AppSecondaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = R.drawable.ic_retry,
        iconEndRes = null,
        enabled = false,
        onClick = {}
    )
}
