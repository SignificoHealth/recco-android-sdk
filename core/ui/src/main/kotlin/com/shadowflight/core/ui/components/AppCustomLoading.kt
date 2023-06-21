package com.shadowflight.core.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadowflight.core.ui.theme.AppTheme


private const val ANIM_DURATION = 1000
private const val TOTAL_DOTS = 4
private const val START_DELAY_DOT = (ANIM_DURATION + (ANIM_DURATION / 1.5f)).toInt() / TOTAL_DOTS

// Starting from the lowest number, each number represent a dot in the screen.
private val DOTS_ANIM_SEQUENCE = listOf(
    0, 1,
    3, 2
)

@Composable
fun AppCustomLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (0 until TOTAL_DOTS step 2).forEach {
                Row {
                    AnimatedDotIcon(delay = START_DELAY_DOT * DOTS_ANIM_SEQUENCE[it])
                    AnimatedDotIcon(delay = START_DELAY_DOT * DOTS_ANIM_SEQUENCE[it + 1])
                }
            }
        }
    }
}

@Composable
private fun AnimatedDotIcon(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = AppTheme.colors.illustration20,
        targetValue = AppTheme.colors.illustration,
        animationSpec = infiniteRepeatable(
            initialStartOffset = StartOffset(
                offsetMillis = delay,
                offsetType = StartOffsetType.Delay
            ),
            animation = tween(
                durationMillis = ANIM_DURATION,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse,
        )
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(28.dp)
            .clip(CircleShape)
            .background(color)
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun Preview() {
    AppCustomLoading()
}
