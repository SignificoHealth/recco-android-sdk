package com.recco.internal.core.ui.extensions

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.recco.internal.core.ui.theme.AppTheme

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

fun Modifier.viewedOverlay(color: Color) = then(
    drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(color = color.copy(alpha = .5f))
        }
    }
)

/**
 * @param index Use for iterable items such as items in a row or column for a sequential effect.
 * @param delayMillis Avoid a non-smooth when the animation starts eagerly during first composition.
 * @param durationMillis Shimmer effect duration.
 * @param syncRatio Allows the shimmer effect be an extension in the following item when supplying the [index].
 */
fun Modifier.shimmerEffect(
    index: Int = 0,
    delayMillis: Int = 1000,
    durationMillis: Int = 2000,
    syncRatio: Int = durationMillis / 16
): Modifier = composed {
    val size = remember { mutableStateOf(IntSize.Zero) }
    val shimmerColors = listOf(
        AppTheme.colors.staticLightGrey,
        AppTheme.colors.staticLightGrey,
        AppTheme.colors.staticLightGrey.copy(alpha = .6f),
        AppTheme.colors.staticLightGrey.copy(alpha = .2f),
        AppTheme.colors.staticLightGrey.copy(alpha = .6f),
        AppTheme.colors.staticLightGrey,
        AppTheme.colors.staticLightGrey
    )
    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = -size.value.height.toFloat(),
        targetValue = 3 * size.value.height.toFloat(),
        animationSpec = infiniteRepeatable(
            initialStartOffset = StartOffset(
                offsetMillis = delayMillis + (index * syncRatio),
                offsetType = StartOffsetType.Delay
            ),
            animation = tween(durationMillis),
            repeatMode = RepeatMode.Restart
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(
                x = translateAnimation.value,
                y = translateAnimation.value
            )
        )
    ).onGloballyPositioned {
        size.value = it.size
    }
}
