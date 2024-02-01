package com.recco.internal.core.ui.components

import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.recco.internal.core.ui.theme.AppTheme

@Composable
fun PlayerSlider(
    modifier: Modifier = Modifier,
    audioDuration: Long,
    currentPosition: Long,
    onSeekPosition: (Long) -> Unit,
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = AppTheme.colors.background,
        activeTrackColor = AppTheme.colors.primary,
        inactiveTrackColor = AppTheme.colors.primary.copy(alpha = 0.2f)
    )
) {
    var sliderPosition by remember {
        mutableStateOf(currentPosition.toFloat())
    }

    Slider(
        value = sliderPosition,
        onValueChange = { newPosition ->
            sliderPosition = newPosition
        },
        onValueChangeFinished = {
            onSeekPosition((sliderPosition * 1000).toLong())
        },
        valueRange = 0f..(audioDuration.coerceAtLeast(0) / 1000).toFloat(),
        steps = 0,
        modifier = modifier,
        colors = colors
    )
}

// TODO Saúl: add a preview
