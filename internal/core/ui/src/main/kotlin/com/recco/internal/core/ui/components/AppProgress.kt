package com.recco.internal.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppSpacing.dp_24
import com.recco.internal.core.ui.theme.AppTheme


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
            .background(AppTheme.colors.background)
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
    color: Color = AppTheme.colors.accent,
    progress: Float? = null,
    animDuration: Int? = null,
    shape: Shape = RoundedCornerShape(0.dp)
) {
    val firstLoad = remember { mutableStateOf(true) }

    if (progress != null) {
        LinearProgressIndicator(
            modifier = modifier
                .clip(shape = shape)
                .height(5.dp)
                .fillMaxWidth(),
            backgroundColor = AppTheme.colors.primary20,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReccoPullRefreshIndicator(
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    scale: Boolean = false,
    elevation: Dp = AppTheme.elevation.default
) {

    Surface(
        modifier = modifier
            .size(dp_24 * 2)
            .pullRefreshIndicatorTransform(state, scale)
            .shadow(elevation = elevation, shape = CircleShape)
            .clip(CircleShape),
        elevation = elevation,
    ) { AppProgressLoadingCircled() }
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

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun SwipeRefreshLoadingIndicatorPreview(
) {
    ReccoPullRefreshIndicator(
        state = rememberPullRefreshState(
            refreshing = false,
            onRefresh = { }
        ),
    )
}

