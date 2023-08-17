package com.recco.showcase.main

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun CustomDropdown(
    expandedState: MutableState<Boolean>,
    topIconMargin: Dp = 0.dp,
    @DrawableRes iconRes: Int,
    collapsableView: @Composable () -> Unit
) {
    val alphaAnim = animateFloat(expandedState.value, value = .25f)
    val rotateAnim = animateFloat(expandedState.value, value = 180f)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .let {
                if (expandedState.value) {
                    it.noRippleClickable { expandedState.value = false }
                } else {
                    it
                }
            }
            .background(Color.Black.copy(alpha = alphaAnim))
            .padding(all = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = topIconMargin)
                .size(36.dp)
                .border(1.dp, color = if (expandedState.value) Color(0x66383B45) else Color.Transparent)
                .noRippleClickable { expandedState.value = !expandedState.value }
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
                    .rotate(rotateAnim),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(WarmBrown)
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(top = topIconMargin),
            visible = expandedState.value,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Surface(
                modifier = Modifier.zIndex(4f),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                collapsableView()
            }
        }
    }
}

@Composable
private fun animateFloat(condition: Boolean, value: Float): Float {
    val anim: Float by updateTransition(
        targetState = condition,
        label = ""
    ).animateFloat(
        transitionSpec = { tween(500) },
        label = ""
    ) { state ->
        if (state) value else 0f
    }

    return anim
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}
