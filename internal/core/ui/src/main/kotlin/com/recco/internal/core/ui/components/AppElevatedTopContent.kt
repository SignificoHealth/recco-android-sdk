package com.recco.internal.core.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.recco.internal.core.ui.extensions.isTopReached
import com.recco.internal.core.ui.theme.AppTheme

@Composable
fun AppElevatedTopContent(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.background,
    content: @Composable () -> Unit
) {
    ElevatedContent(
        modifier = modifier,
        color = color,
        elevation = AppTheme.elevation.default,
        content = content
    )
}

@Composable
private fun ElevatedContent(
    modifier: Modifier = Modifier,
    color: Color,
    elevation: Dp,
    content: @Composable () -> Unit

) {
    Surface(
        modifier = modifier.zIndex(4f),
        color = color,
        elevation = elevation
    ) {
        content()
    }
}

@Composable
fun BoxScope.AppTopShadow(scrollState: ScrollState) {
    val animateElevation = animateDpAsState(
        targetValue = if (scrollState.isTopReached()) 0.dp else AppTheme.elevation.default
    )

    TopShadow(animateElevation.value)
}

@Composable
fun BoxScope.AppTopShadow(scrollState: LazyListState) {
    val animateElevation = animateDpAsState(
        targetValue = if (scrollState.isTopReached()) 0.dp else AppTheme.elevation.default
    )

    TopShadow(animateElevation.value)
}

@Composable
private fun BoxScope.TopShadow(elevation: Dp) {
    Spacer(
        modifier = Modifier
            .zIndex(4f)
            .fillMaxWidth()
            .height(elevation)
            .align(Alignment.TopCenter)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AppTheme.colors.shadow,
                        Color.Transparent
                    )
                )
            )
    )
}
