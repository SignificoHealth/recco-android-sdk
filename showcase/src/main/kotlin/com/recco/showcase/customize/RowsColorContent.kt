package com.recco.showcase.customize

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.api.model.ReccoPalette
import com.recco.showcase.R
import com.recco.showcase.data.mappers.asShowcasePalette
import com.recco.showcase.main.asComposeColor
import com.recco.showcase.models.ShowcaseColors
import com.recco.showcase.models.ShowcasePalette
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun RowsColorContent(
    palette: ShowcasePalette,
    onUpdatePalette: (ShowcasePalette) -> Unit,
    isDarkMode: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val colors = if (isDarkMode) palette.darkColors else palette.lightColors
        val update: (ShowcaseColors) -> Unit = {
            onUpdatePalette(
                if (isDarkMode) {
                    palette.copy(darkColors = it)
                } else {
                    palette.copy(lightColors = it)
                }
            )
        }
        val marginBetweenRows = 32.dp

        RowColor(
            updateColor = { color -> update(colors.copy(primary = color)) },
            updateOnColor = { color -> update(colors.copy(onPrimary = color)) },
            color = colors.primary,
            onColor = colors.onPrimary,
            nameColor = R.string.primary,
            nameOnColor = R.string.on_primary
        )
        Spacer(Modifier.height(marginBetweenRows))

        RowColor(
            updateColor = { color -> update(colors.copy(background = color)) },
            updateOnColor = { color -> update(colors.copy(onBackground = color)) },
            color = colors.background,
            onColor = colors.onBackground,
            nameColor = R.string.background,
            nameOnColor = R.string.on_background
        )
        Spacer(Modifier.height(marginBetweenRows))

        RowColor(
            updateColor = { color -> update(colors.copy(accent = color)) },
            updateOnColor = { color -> update(colors.copy(onAccent = color)) },
            color = colors.accent,
            onColor = colors.onAccent,
            nameColor = R.string.accent,
            nameOnColor = R.string.on_accent
        )
        Spacer(Modifier.height(marginBetweenRows))

        RowColor(
            updateColor = { color -> update(colors.copy(illustration = color)) },
            updateOnColor = { color -> update(colors.copy(illustrationOutline = color)) },
            color = colors.illustration,
            onColor = colors.illustrationOutline,
            nameColor = R.string.illustration,
            nameOnColor = R.string.illustration_outline
        )
        Spacer(Modifier.height(marginBetweenRows))
    }
}

@Composable
private fun RowColor(
    updateColor: (hex: String) -> Unit,
    updateOnColor: (hex: String) -> Unit,
    color: String,
    onColor: String,
    nameColor: Int,
    nameOnColor: Int
) {
    val showColorPickerState = remember { mutableStateOf(false) }
    val onColorBeingUpdate = remember { mutableStateOf(ColorBeingUpdate(color = color, isOnColor = false)) }

    ColorPickerScreen(
        showState = showColorPickerState,
        color = onColorBeingUpdate.value.color,
        updateColor = { newColor ->
            if (onColorBeingUpdate.value.isOnColor) {
                updateOnColor(newColor)
            } else {
                updateColor(newColor)
            }
        }
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .clickable {
                    onColorBeingUpdate.value = ColorBeingUpdate(color = color, isOnColor = false)
                    showColorPickerState.value = true
                }
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = stringResource(nameColor),
                style = Typography.titleMedium.copy(color = WarmBrown)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopCenter)
                    .background(color.asComposeColor())
            )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .clickable {
                    onColorBeingUpdate.value = ColorBeingUpdate(color = onColor, isOnColor = true)
                    showColorPickerState.value = true
                }
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = stringResource(nameOnColor),
                style = Typography.titleMedium.copy(color = WarmBrown)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(onColor.asComposeColor())
            )
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterEnd)
                .background(color.asComposeColor())
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
                    .background(onColor.asComposeColor())
            )
        }
    }
}

private data class ColorBeingUpdate(
    val color: String,
    val isOnColor: Boolean
)

@Preview
@Composable
private fun Preview() {
    RowsColorContent(palette = ReccoPalette.Fresh.asShowcasePalette(), onUpdatePalette = {}, isDarkMode = false)
}

@Preview
@Composable
private fun PreviewDarkMode() {
    RowsColorContent(palette = ReccoPalette.Fresh.asShowcasePalette(), onUpdatePalette = {}, isDarkMode = false)
}
