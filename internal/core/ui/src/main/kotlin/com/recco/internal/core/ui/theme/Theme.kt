package com.recco.internal.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.recco.internal.core.ui.components.AppStatusBar
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.recco.api.model.Palette

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorStatusBar: Color = AppTheme.colors.primary,
    palette: Palette = Palette.DEFAULT,
    content: @Composable () -> Unit
) {
    val colors = when (palette) {
        Palette.DEFAULT -> if (darkTheme) darkColors else lightColors
    }

    AppStatusBar(color = colorStatusBar, darkIcons = darkTheme)

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
