package com.shadowflight.uicommons.theme

import androidx.compose.material.lightColors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val primary = Color(0xFF453838)
val primary40 = primary.copy(alpha = .4f)
val primary20 = primary.copy(alpha = .2f)
val primary10 = primary.copy(alpha = .1f)
val onPrimary = Color(0xFFF6F190)
val onPrimary60 = onPrimary.copy(alpha = .6f)
val onPrimary20 = onPrimary.copy(alpha = .2f)
val surface = Color(0xFFDBDCF0)
val onSurface = Color(0xFF463738)
val background = Color(0xFFFCFCFC)
val onBackground = Color(0xFF463738)

@Immutable
data class ExtendedColors(
    val primary: Color,
    val primary40: Color,
    val primary20: Color,
    val primary10: Color,
    val onPrimary: Color,
    val onPrimary60: Color,
    val onPrimary20: Color,
    val surface: Color,
    val onSurface: Color,
    val background: Color,
    val onBackground: Color
)

internal val lightColors = lightColors(
    primary = primary,
    primaryVariant = primary40,
    secondary = primary,
    secondaryVariant = primary40,
    background = background,
    surface = background,
    onPrimary = onPrimary,
    onSecondary = onPrimary,
    onBackground = onBackground,
    onSurface = onBackground,
    onError = Color.White
)

internal val extendedColors = ExtendedColors(
    primary = primary,
    primary40 = primary40,
    primary20 = primary20,
    primary10 = primary10,
    onPrimary = onPrimary,
    onPrimary60 = onPrimary60,
    onPrimary20 = onPrimary20,
    surface = surface,
    onSurface = onSurface,
    background = background,
    onBackground = onBackground
)

internal val LocalExtendedColors = staticCompositionLocalOf {
    extendedColors
}

