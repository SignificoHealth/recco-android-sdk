package com.shadowflight.uicommons.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shadowflight.uicommons.extensions.isEndReached
import com.shadowflight.uicommons.theme.AppTheme

private val DEFAULT_ELEVATION = 4.dp

@Composable
fun AppElevatedBottomContent(scrollState: ScrollState, content: @Composable () -> Unit) {
    val animateElevation = animateDpAsState(
        targetValue = if (scrollState.isEndReached()) 0.dp else DEFAULT_ELEVATION
    )

    Surface(
        modifier = Modifier.zIndex(4f),
        elevation = animateElevation.value
    ) {
        content()
    }
}

@Composable
fun AppElevatedBottomContent(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.zIndex(4f),
        elevation = DEFAULT_ELEVATION
    ) {
        content()
    }
}

@Composable
fun AppBottomShadow(scrollState: ScrollState) {
    val animateElevation = animateDpAsState(
        targetValue = if (scrollState.isEndReached()) 0.dp else DEFAULT_ELEVATION / 2
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(animateElevation.value)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        AppTheme.colors.shadow,
                    )
                )
            )
    )
}
