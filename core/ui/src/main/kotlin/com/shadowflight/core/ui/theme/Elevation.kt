package com.shadowflight.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Elevation(
    val default: Dp,
    val pressed: Dp,
    val card: Dp
)

internal val LocalElevation = staticCompositionLocalOf {
    Elevation(
        default = Dp.Unspecified,
        pressed = Dp.Unspecified,
        card = Dp.Unspecified
    )
}

internal val elevation = Elevation(
    default = 4.dp,
    pressed = 8.dp,
    card = 4.dp
)
