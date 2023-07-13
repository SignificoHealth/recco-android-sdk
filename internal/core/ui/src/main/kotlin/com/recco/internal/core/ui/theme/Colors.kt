package com.recco.internal.core.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.recco.api.model.ReccoColors

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

fun ReccoColors.asExtendedColors() = ExtendedColors(
    primary = primary,
    primary80 = primary.copy(alpha = .8f),
    primary60 = primary.copy(alpha = .6f),
    primary40 = primary.copy(alpha = .4f),
    primary20 = primary.copy(alpha = .2f),
    primary10 = primary.copy(alpha = .1f),
    onPrimary = onPrimary,
    background = background,
    onBackground = onBackground,
    onBackground60 = onBackground.copy(alpha = .6f),
    onBackground20 = onBackground.copy(alpha = .2f),
    accent = accent,
    accent60 = accent.copy(alpha = .6f),
    accent40 = accent.copy(alpha = .4f),
    accent20 = accent.copy(alpha = .2f),
    accent10 = accent.copy(alpha = .1f),
    onAccent = onAccent,
    illustration = illustration,
    illustration80 = illustration.copy(alpha = .8f),
    illustration40 = illustration.copy(alpha = .4f),
    illustration20 = illustration.copy(alpha = .2f),
    surface = surface,
    onSurface = onSurface,
    error = Color(0xFFB00020),
    onError = Color.White,
    shadow = Color(0xFF000000).copy(alpha = .06f),
    lightGrey = Color(0xFFEBEBEB)
)

fun ExtendedColors.asColor(isLight: Boolean) = Colors(
    primary,
    primary40,
    primary,
    primary40,
    background,
    surface,
    error,
    onPrimary,
    onPrimary,
    onBackground,
    onSurface,
    onError,
    isLight
)