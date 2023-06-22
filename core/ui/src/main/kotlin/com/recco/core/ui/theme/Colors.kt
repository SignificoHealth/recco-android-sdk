package com.recco.core.ui.theme

import androidx.compose.material.lightColors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val primary = Color(0xFF383B45)
internal val primary80 = primary.copy(alpha = .8f)
internal val primary60 = primary.copy(alpha = .6f)
internal val primary40 = primary.copy(alpha = .4f)
internal val primary20 = primary.copy(alpha = .2f)
internal val primary10 = primary.copy(alpha = .1f)
internal val onPrimary = Color(0xFFFFFFFF)
internal val background = Color(0xFFFCFCFC)
internal val onBackground = Color(0xFF383B45)
internal val onBackground60 = onBackground.copy(alpha = .6f)
internal val onBackground20 = onBackground.copy(alpha = .2f)
internal val accent = Color(0xFF7B61FF)
internal val accent60 = accent.copy(alpha = .6f)
internal val accent40 = accent.copy(alpha = .4f)
internal val accent20 = accent.copy(alpha = .2f)
internal val accent10 = accent.copy(alpha = .1f)
internal val onAccent = Color(0xFF2C2783)
internal val illustration = Color(0xFFF5B731)
internal val illustration80 = illustration.copy(alpha = .8f)
internal val illustration40 = illustration.copy(alpha = .4f)
internal val illustration20 = illustration.copy(alpha = .2f)
internal val surface = Color(0xFFDAD7D7)
internal val onSurface = Color(0xFF383B45)

internal val error: Color = Color(0xFFB00020)
internal val onError: Color = Color.White
internal val shadow06: Color = Color(0xFF000000).copy(alpha = .06f)
internal val lightGrey: Color = Color(0xFFEBEBEB)


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
    val illustration80: Color,
    val illustration40: Color,
    val illustration20: Color,
    val surface: Color,
    val onSurface: Color,

    val error: Color,
    val onError: Color,
    val shadow: Color,
    val lightGrey: Color,
)

internal val lightColors = lightColors(
    primary = primary,
    primaryVariant = primary40,
    secondary = primary,
    secondaryVariant = primary40,
    background = background,
    surface = background,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onPrimary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError
)

internal val darkColors = lightColors

internal val extendedColors = ExtendedColors(
    primary = primary,
    primary80 = primary80,
    primary60 = primary60,
    primary40 = primary40,
    primary20 = primary20,
    primary10 = primary10,
    onPrimary = onPrimary,
    background = background,
    onBackground = onBackground,
    onBackground60 = onBackground60,
    onBackground20 = onBackground20,
    accent = accent,
    accent60 = accent60,
    accent40 = accent40,
    accent20 = accent20,
    accent10 = accent10,
    onAccent = onAccent,
    illustration = illustration,
    illustration80 = illustration80,
    illustration40 = illustration40,
    illustration20 = illustration20,
    surface = surface,
    onSurface = onSurface,
    error = error,
    onError = onError,
    shadow = shadow06,
    lightGrey = lightGrey
)

internal val extendedLightColors = extendedColors
internal val extendedDarkColors = extendedColors

internal val LocalExtendedColors = staticCompositionLocalOf {
    extendedColors
}