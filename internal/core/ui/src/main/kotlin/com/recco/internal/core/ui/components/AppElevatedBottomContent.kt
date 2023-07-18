package com.recco.internal.core.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.recco.internal.core.ui.extensions.isEndReached
import com.recco.internal.core.ui.theme.AppTheme

@Composable
fun AppElevatedBottomContent(scrollState: ScrollState, content: @Composable () -> Unit) {
    val animateElevation = animateDpAsState(
        targetValue = if (scrollState.isEndReached()) 0.dp else AppTheme.elevation.default
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
        elevation = AppTheme.elevation.default
    ) {
        content()
    }
}

@Composable
fun BoxScope.AppBottomShadow(scrollState: ScrollState) {
    val animateElevation = animateDpAsState(
        targetValue = if (scrollState.isEndReached()) 0.dp else AppTheme.elevation.default / 2
    )

    Spacer(
        modifier = Modifier
            .zIndex(4f)
            .fillMaxWidth()
            .height(animateElevation.value)
            .align(Alignment.BottomCenter)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        AppTheme.colors.shadow
                    )
                )
            )
    )
}
