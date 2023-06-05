package com.shadowflight.core.ui.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color

fun Modifier.viewedOverlay(color: Color) = then(
    drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(color = color.copy(alpha = .5f))
        }
    }
)