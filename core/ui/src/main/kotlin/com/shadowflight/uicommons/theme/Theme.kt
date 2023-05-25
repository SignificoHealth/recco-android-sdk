package com.shadowflight.uicommons.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalExtendedTypography provides extendedTypography,
        LocalElevation provides elevation
    ) {
        MaterialTheme(
            colors = lightColors,
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
