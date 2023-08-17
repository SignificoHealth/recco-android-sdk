package com.recco.internal.core.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.recco.api.model.ReccoColors
import com.recco.api.model.ReccoPalette

@Immutable
data class ExtendedColors(
    val primary: Color,
    val primary80: Color,
    val primary60: Color,
    val primary40: Color,
    val primary20: Color,
    val primary10: Color,
    val onPrimary: Color,
    val background: Color,
    val onBackground: Color,
    val onBackground60: Color,
    val onBackground20: Color,
    val accent: Color,
    val accent60: Color,
    val accent40: Color,
    val accent20: Color,
    val accent10: Color,
    val onAccent: Color,
    val illustration: Color,
    val illustrationOutline: Color,
    val illustration80: Color,
    val illustration40: Color,
    val illustration20: Color,
    val surface: Color,
    val onSurface: Color,
    val shadow: Color,
    val staticLightGrey: Color,
    val staticDark: Color
)

fun ReccoColors.asExtendedColors() = ExtendedColors(
    primary = primary.asComposeColor(),
    primary80 = primary.asComposeColor().copy(alpha = .8f),
    primary60 = primary.asComposeColor().copy(alpha = .6f),
    primary40 = primary.asComposeColor().copy(alpha = .4f),
    primary20 = primary.asComposeColor().copy(alpha = .2f),
    primary10 = primary.asComposeColor().copy(alpha = .1f),
    onPrimary = onPrimary.asComposeColor(),
    background = background.asComposeColor(),
    onBackground = onBackground.asComposeColor(),
    onBackground60 = onBackground.asComposeColor().copy(alpha = .6f),
    onBackground20 = onBackground.asComposeColor().copy(alpha = .2f),
    accent = accent.asComposeColor(),
    accent60 = accent.asComposeColor().copy(alpha = .6f),
    accent40 = accent.asComposeColor().copy(alpha = .4f),
    accent20 = accent.asComposeColor().copy(alpha = .2f),
    accent10 = accent.asComposeColor().copy(alpha = .1f),
    onAccent = onAccent.asComposeColor(),
    illustration = illustration.asComposeColor(),
    illustration80 = illustration.asComposeColor().copy(alpha = .8f),
    illustration40 = illustration.asComposeColor().copy(alpha = .4f),
    illustration20 = illustration.asComposeColor().copy(alpha = .2f),
    illustrationOutline = illustrationOutline.asComposeColor(),
    surface = Color(0xFFDAD7D7),
    onSurface = Color(0xFF383B45),
    shadow = Color(0xFF000000).copy(alpha = .06f),
    staticLightGrey = Color(0xFFEBEBEB),
    staticDark = Color(0xFF383b45)
)

fun ExtendedColors.asColor(isLight: Boolean) = Colors(
    primary,
    primary40,
    primary,
    primary40,
    background,
    surface,
    primary,
    onPrimary,
    onPrimary,
    onBackground,
    onSurface,
    onPrimary,
    isLight
)

internal val LocalExtendedColors = staticCompositionLocalOf {
    ReccoPalette.Fresh.lightColors.asExtendedColors()
}

private fun String.asComposeColor() = Color(android.graphics.Color.parseColor(this))
