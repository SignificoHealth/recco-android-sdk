package com.shadowflight.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme

// width/height positive ratio
const val ASPECT_RATIO_1_1 = 1f
const val ASPECT_RATIO_6_5 = 6f / 5f // 0.83
const val ASPECT_RATIO_4_3 = 4f / 3f // 0.75
const val ASPECT_RATIO_10_7 = 10f / 7f // 0.7
const val ASPECT_RATIO_16_9 = 16f / 9f // 0.56
const val ASPECT_RATIO_10_5 = 10f / 5f // 0.5
const val ASPECT_RATIO_10_4 = 10f / 4f // 0.4


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