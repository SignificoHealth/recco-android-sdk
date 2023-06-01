package com.shadowflight.uicommons.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.shadowflight.uicommons.theme.AppSpacing
import com.shadowflight.uicommons.theme.AppTheme


@Composable
fun AppProgressLoading(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.primary,
    size: Dp = 48.dp,
    strokeWidth: Dp = 3.dp,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth,
    )
}

@Composable
fun AppProgressLoadingCircled(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.primary,
    size: Dp = 48.dp,
    strokeWidth: Dp = 3.dp,
    elevation: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation = elevation, shape = CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .padding(AppSpacing.dp_8),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            color = color,
            strokeWidth = strokeWidth,
        )
    }
}

@Composable
fun AppLinearProgress(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.primary,
    progress: Float? = null,
    animDuration: Int? = null,
    shape: Shape = RoundedCornerShape(0.dp)
) {
    val firstLoad = remember { mutableStateOf(true) }

    if (progress != null) {
        LinearProgressIndicator(
            modifier = modifier
                .clip(shape = shape)
                .fillMaxWidth(),
            backgroundColor = AppTheme.colors.background,
            color = color,
            progress = if (animDuration != null && !firstLoad.value) {
                val progressAnimated by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(
                        durationMillis = animDuration,
                        easing = LinearEasing
                    )
                )
                progressAnimated
            } else {
                firstLoad.value = false
                progress
            }
        )
    } else {
        LinearProgressIndicator(
            modifier = modifier
                .clip(shape = shape)
                .fillMaxWidth(),
            backgroundColor = color,
            color = color
        )
    }
}

@Composable
fun AppSwipeRefreshLoadingIndicator(
    state: SwipeRefreshState,
    refreshTrigger: Dp,
    elevation: Dp = 4.dp
) {
    val sizeDp = 48.dp
    val sizePx = with(LocalDensity.current) { sizeDp.toPx() }
    val triggerPx = with(LocalDensity.current) { refreshTrigger.toPx().toInt() }
    val isRefresh = state.indicatorOffset.toInt() > triggerPx

    val offset = when {
        isRefresh -> triggerPx + (state.indicatorOffset * .1f)
        state.isRefreshing -> triggerPx
        else -> state.indicatorOffset  // is swiping
    }
    val elevationPx = with(LocalDensity.current) { elevation.toPx() }

    AppProgressLoadingCircled(
        modifier = Modifier
            .size(sizeDp)
            .offset { IntOffset(x = 0, y = -(sizePx + elevationPx).toInt()) }
            .offset { IntOffset(x = 0, y = offset.toInt()) },
        elevation = elevation
    )
}

@Preview
@Composable
private fun AppProgressLoadingPreview(
) {
    AppProgressLoading()
}

@Preview
@Composable
private fun AppProgressLoadingCircledPreview(
) {
    AppProgressLoadingCircled()
}

@Preview
@Composable
private fun LinearProgressPreview(
) {
    AppLinearProgress(progress = .5f)
}

@Preview
@Composable
private fun SwipeRefreshLoadingIndicatorPreview(
) {
    AppSwipeRefreshLoadingIndicator(
        state = SwipeRefreshState(isRefreshing = true),
        refreshTrigger = 100.dp
    )
}
