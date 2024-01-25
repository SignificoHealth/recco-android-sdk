package com.recco.internal.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp

/**
 * Measures the width of a given text with a specific style and returns it as Dp.
 *
 * @param text The text to measure.
 * @param style The [TextStyle] applied to the text.
 * @return The width of the text in [Dp].
 */
@Composable
fun MeasureTextWidth(text: String, style: TextStyle): Dp {
    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(text, style).size.width
    return with(LocalDensity.current) { widthInPixels.toDp() }
}
