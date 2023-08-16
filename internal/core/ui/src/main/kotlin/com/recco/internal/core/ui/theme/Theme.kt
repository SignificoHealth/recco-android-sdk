package com.recco.internal.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.recco.api.model.ReccoStyle
import com.recco.internal.core.ui.components.AppStatusBar

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    style: ReccoStyle = ReccoStyle(),
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) {
        style.palette.darkColors.asExtendedColors()
    } else {
        style.palette.lightColors.asExtendedColors()
    }

    AppStatusBar(color = AppTheme.colors.staticDark, darkIcons = false)

    val fontFamily = style.font.asFontFamily()

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography(extendedColors.primary, fontFamily),
        LocalElevation provides elevation
    ) {
        MaterialTheme(
            colors = extendedColors.asColor(isLight = !darkTheme),
            typography = extendedTypography(extendedColors.primary, fontFamily).asTypography()
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
