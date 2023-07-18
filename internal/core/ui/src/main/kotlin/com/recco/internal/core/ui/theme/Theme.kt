package com.recco.internal.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.recco.api.model.ReccoPalette
import com.recco.internal.core.ui.components.AppStatusBar

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    palette: ReccoPalette = ReccoPalette.Fresh,
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) {
        palette.darkColors.asExtendedColors()
    } else {
        palette.lightColors.asExtendedColors()
    }

    AppStatusBar(color = AppTheme.colors.staticDark, darkIcons = false)

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography(extendedColors.primary),
        LocalElevation provides elevation
    ) {
        MaterialTheme(
            colors = extendedColors.asColor(isLight = !darkTheme),
            typography = extendedTypography(extendedColors.primary).asTypography()
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
