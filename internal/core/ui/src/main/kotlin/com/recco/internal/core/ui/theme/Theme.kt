package com.recco.internal.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.recco.api.model.ReccoPalette
import com.recco.internal.core.ui.components.AppStatusBar

internal val LocalExtendedColors = staticCompositionLocalOf {
    ReccoPalette.Default.lightColors.asExtendedColors()
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorStatusBar: Color = AppTheme.colors.primary,
    palette: ReccoPalette = ReccoPalette.Default,
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) {
        palette.darkColors.asExtendedColors()
    } else {
        palette.lightColors.asExtendedColors()
    }

    val colors = extendedColors.asColor(isLight = !darkTheme)

    AppStatusBar(color = colorStatusBar, darkIcons = darkTheme)

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography,
        LocalElevation provides elevation
    ) {
        MaterialTheme(
            colors = colors,
            typography = typography
        ) {
            CompositionLocalProvider(content = content)
        }
    }
}

object AppTheme {
    val colors: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current
    val typography: ExtendedTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedTypography.current
    val elevation: Elevation
        @Composable
        @ReadOnlyComposable
        get() = LocalElevation.current
}
