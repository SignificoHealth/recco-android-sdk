package com.shadowflight.core.ui.components

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shadowflight.core.ui.extensions.isTopReached
import com.shadowflight.core.ui.theme.AppTheme

private val DEFAULT_ELEVATION = 4.dp

@Composable
fun AppElevatedTopContent(
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    showElevationCondition: () -> Boolean = { !scrollState.isTopReached() },
    content: @Composable () -> Unit
) {
    val showElevation by remember {
        derivedStateOf { showElevationCondition() }
    }
    val animateElevation = animateDpAsState(
        targetValue = if (showElevation) DEFAULT_ELEVATION else 0.dp
    )

    ElevatedContent(
        modifier = modifier,
        color = color,
        elevation = animateElevation.value,
        content = content
    )
}

@Composable
fun AppElevatedTopContent(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    showElevationCondition: () -> Boolean = { !scrollState.isTopReached() },
    content: @Composable () -> Unit
) {
    val showElevation by remember {
        derivedStateOf { showElevationCondition() }
    }
    val animateElevation = animateDpAsState(
        targetValue = if (showElevation) DEFAULT_ELEVATION else 0.dp
    )

    ElevatedContent(
        modifier = modifier,
        color = color,
        elevation = animateElevation.value,
        content = content
    )
}

@Composable
fun AppElevatedTopContent(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    content: @Composable () -> Unit
) {
    ElevatedContent(
        modifier = modifier,
        color = color,
        elevation = DEFAULT_ELEVATION,
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
        targetValue = if (scrollState.isTopReached()) 0.dp else DEFAULT_ELEVATION
    )

    TopShadow(animateElevation.value)
}

@Composable
fun BoxScope.AppTopShadow(scrollState: LazyListState) {
    val animateElevation = animateDpAsState(
        targetValue = if (scrollState.isTopReached()) 0.dp else DEFAULT_ELEVATION
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
                        Color.Transparent,
                    )
                )
            )
    )
}
