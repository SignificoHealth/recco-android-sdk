package com.shadowflight.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.shadowflight.core.ui.components.AppStatusBar

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorStatusBar: Color = Color.White,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColors else lightColors

    AppStatusBar(colorStatusBar)

    CompositionLocalProvider(
        LocalExtendedColors provides if (darkTheme) extendedDarkColors else extendedLightColors,
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
