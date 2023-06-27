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
fun AppPrimaryButton(
    modifier: Modifier = Modifier,
    isOverBackground: Boolean = false,
    @StringRes textRes: Int? = null,
    @DrawableRes iconStartRes: Int? = null,
    @DrawableRes iconEndRes: Int? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    AppButton(
        modifier = modifier,
        isPrimary = true,
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
fun AppPrimaryButton(
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
        isPrimary = true,
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
private fun AppPrimaryButtonActiveOverLightBackgroundPreview() {
    AppPrimaryButton(
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
private fun AppPrimaryButtonInactiveOverLightBackgroundPreview() {
    AppPrimaryButton(
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
private fun AppPrimaryButtonActiveWithEndIconOverLightBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = false,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.recco_ic_retry,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppPrimaryButtonInactiveWithEndIconOverLightBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = false,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.recco_ic_retry,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppPrimaryButtonActiveWithStartIconOverLightBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = false,
        text = "Button",
        iconStartRes = R.drawable.recco_ic_retry,
        iconEndRes = null,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun AppPrimaryButtonInactiveWithStartIconOverLightBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = false,
        text = "Button",
        iconStartRes = R.drawable.recco_ic_retry,
        iconEndRes = null,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppPrimaryButtonActiveOverDarkBackgroundPreview() {
    AppPrimaryButton(
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
private fun AppPrimaryButtonInactiveOverDarkBackgroundPreview() {
    AppPrimaryButton(
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
private fun AppPrimaryButtonActiveWithEndIconOverDarkBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.recco_ic_retry,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppPrimaryButtonInactiveWithEndIconOverDarkBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = null,
        iconEndRes = R.drawable.recco_ic_retry,
        enabled = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppPrimaryButtonActiveWithStartIconOverDarkBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = R.drawable.recco_ic_retry,
        iconEndRes = null,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0x5760CC)
@Composable
private fun AppPrimaryButtonInactiveWithStartIconOverDarkBackgroundPreview() {
    AppPrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        isOverBackground = true,
        text = "Button",
        iconStartRes = R.drawable.recco_ic_retry,
        iconEndRes = null,
        enabled = false,
        onClick = {}
    )
}