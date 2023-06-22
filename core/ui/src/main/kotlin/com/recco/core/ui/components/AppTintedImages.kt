package com.recco.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.recco.core.ui.R
import com.recco.core.ui.theme.AppTheme

@Composable
fun AppTintedImagePottedPlant2(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_potted_plant_2,
        drawableResTint = R.drawable.ic_potted_plant_2_tint,
        tint = tint,
    )
}

@Composable
fun AppTintedImageApple(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_apple,
        drawableResTint = R.drawable.ic_apple_tint,
        tint = tint,
    )
}

@Composable
fun AppTintedImageNoConnection(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_no_connection, // FIXME
        drawableResTint = R.drawable.ic_no_connection_tint,
        tint = tint,
    )
}

@Composable
fun AppTintedImagePeopleDigital(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_people_digital, // FIXME
        drawableResTint = R.drawable.ic_people_digital_tint,
        tint = tint,
    )
}

@Composable
fun AppTintedImageRidingBike(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_riding_bike, // FIXME
        drawableResTint = R.drawable.ic_riding_bike_tint,
        tint = tint,
    )
}

@Composable
fun AppTintedImageAboutYou(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_about_you, // FIXME
        drawableResTint = R.drawable.ic_about_you_tint,
        tint = tint,
    )
}

@Composable
fun AppTintedImagePortrait1(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_portrait_1, // FIXME
        drawableResTint = R.drawable.ic_portrait_1_tint,
        tint = tint,
    )
}

@Composable
fun AppTintedImagePortrait2(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.illustration
) {
    AppTintedImage(
        modifier = modifier,
        drawableRes = R.drawable.ic_portrait_2, // FIXME
        drawableResTint = R.drawable.ic_portrait_2_tint,
        tint = tint,
    )
}

@Composable
private fun AppTintedImage(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    @DrawableRes drawableResTint: Int,
    tint: Color
) {
    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(drawableResTint),
            colorFilter = ColorFilter.tint(tint),
            contentDescription = null,
        )
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(drawableRes),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun PreviewAppTintedImagePottedPlant2() {
    Column {
        AppTintedImagePottedPlant2()
        AppTintedImagePottedPlant2(tint = AppTheme.colors.error)
    }
}

@Preview
@Composable
private fun PreviewAppTintedImageApple() {
    Column {
        AppTintedImageApple()
        AppTintedImageApple(tint = AppTheme.colors.error)
    }
}