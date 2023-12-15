package com.recco.internal.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.recco.api.model.ReccoStyle
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.preview.ReccoStyleProvider
import com.recco.internal.core.ui.theme.AppTheme

@Composable
fun AppTintedImagePottedPlant(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_potted_plant_static,
        drawableResTint = R.drawable.recco_ic_potted_plant_tint,
        drawableResOutline = R.drawable.recco_ic_potted_plant_outline
    )
}

@Composable
fun AppTintedImageNoConnection(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_no_connection_static,
        drawableResTint = R.drawable.recco_ic_no_connection_tint,
        drawableResOutline = R.drawable.recco_ic_no_connection_outline
    )
}

@Composable
fun AppTintedImagePeopleDigital(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_people_digital_static,
        drawableResTint = R.drawable.recco_ic_people_digital_tint,
        drawableResOutline = R.drawable.recco_ic_people_digital_outline
    )
}

@Composable
fun AppTintedImageSleep(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_sleep_static,
        drawableResTint = R.drawable.recco_ic_sleep_tint,
        drawableResOutline = R.drawable.recco_ic_sleep_outline
    )
}

@Composable
fun AppTintedImageEating(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_eating_static,
        drawableResTint = R.drawable.recco_ic_eating_tint,
        drawableResOutline = R.drawable.recco_ic_eating_outline
    )
}

@Composable
fun AppTintedImageActivity(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_activity_static,
        drawableResTint = R.drawable.recco_ic_activity_tint,
        drawableResOutline = R.drawable.recco_ic_activity_outline
    )
}

@Composable
fun AppTintedImagePortrait(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_portrait_static,
        drawableResTint = R.drawable.recco_ic_portrait_tint,
        drawableResOutline = R.drawable.recco_ic_portrait_outline
    )
}

@Composable
fun AppTintedImageFlying(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_flying_static,
        drawableResTint = R.drawable.recco_ic_flying_tint,
        drawableResOutline = R.drawable.recco_ic_flying_outline
    )
}

@Composable
fun AppTintedImageContent(
    modifier: Modifier = Modifier
) {
    AppTintedImage(
        modifier = modifier,
        drawableResStatic = R.drawable.recco_ic_content_static,
        drawableResTint = R.drawable.recco_ic_content_tint,
        drawableResOutline = R.drawable.recco_ic_content_outline
    )
}

@Composable
private fun AppTintedImage(
    modifier: Modifier = Modifier,
    @DrawableRes drawableResStatic: Int,
    @DrawableRes drawableResTint: Int,
    @DrawableRes drawableResOutline: Int
) {
    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(drawableResTint),
            colorFilter = ColorFilter.tint(AppTheme.colors.illustration),
            contentDescription = null
        )
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(drawableResStatic),
            contentDescription = null
        )
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(drawableResOutline),
            colorFilter = ColorFilter.tint(AppTheme.colors.illustrationOutline),
            contentDescription = null
        )
    }
}

@Preview(heightDp = 1500)
@Composable
private fun PreviewLight(
    @PreviewParameter(ReccoStyleProvider::class) style: ReccoStyle
) {
    AppTheme(darkTheme = false, style = style) {
        Column {
            AppTintedImagePottedPlant()
            AppTintedImageNoConnection()
            AppTintedImagePeopleDigital()
            AppTintedImagePortrait()
            AppTintedImageFlying()
            AppTintedImageContent()
            AppTintedImageSleep()
            AppTintedImageEating()
            AppTintedImageActivity()
        }
    }
}

@Preview(heightDp = 1500)
@Composable
private fun PreviewDark(
    @PreviewParameter(ReccoStyleProvider::class) style: ReccoStyle
) {
    AppTheme(darkTheme = true, style = style) {
        Column(Modifier.background(AppTheme.colors.background)) {
            AppTintedImagePottedPlant()
            AppTintedImageNoConnection()
            AppTintedImagePeopleDigital()
            AppTintedImagePortrait()
            AppTintedImageFlying()
            AppTintedImageContent()
            AppTintedImageSleep()
            AppTintedImageEating()
            AppTintedImageActivity()
        }
    }
}
