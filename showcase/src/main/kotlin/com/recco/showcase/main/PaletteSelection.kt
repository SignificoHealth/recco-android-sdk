package com.recco.showcase.main

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.recco.api.model.ReccoPalette
import com.recco.showcase.R
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun PaletteSelection(
    selectionClickPalette: (ReccoPalette) -> Unit,
    initiallyExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    val alphaAnim = animateFloat(expanded, value = .25f)
    val rotateAnim = animateFloat(expanded, value = 180f)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .let {
                if (expanded) {
                    it.noRippleClickable { expanded = false }
                } else {
                    it
                }
            }
            .background(Color.Black.copy(alpha = alphaAnim))
            .padding(all = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .border(1.dp, color = if (expanded) Color(0x66383B45) else Color.Transparent)
                .noRippleClickable { expanded = !expanded }
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
                    .rotate(rotateAnim),
                painter = painterResource(R.drawable.ic_palette),
                contentDescription = null
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Surface(
                modifier = Modifier.zIndex(4f),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                Column {
                    MosaicSampleSection(ReccoPalette.Fresh, showIconHeader = true,
                        selectionClickPalette = {
                            expanded = false
                            selectionClickPalette(it)
                        }
                    )
                    MosaicSampleSection(ReccoPalette.Ocean, showIconHeader = false,
                        selectionClickPalette = {
                            expanded = false
                            selectionClickPalette(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MosaicSampleSection(
    palette: ReccoPalette,
    showIconHeader: Boolean,
    selectionClickPalette: (ReccoPalette) -> Unit
) {
    Row(Modifier
        .noRippleClickable { selectionClickPalette(palette) }
    ) {
        MosaicSample(
            palette = palette,
            showIconHeader = showIconHeader,
            isDarkMode = false
        )

        MosaicSample(
            palette = palette,
            showIconHeader = showIconHeader,
            isDarkMode = true
        )
    }
}

@Composable
fun MosaicSample(
    palette: ReccoPalette,
    showIconHeader: Boolean,
    isDarkMode: Boolean
) {
    val paletteSize = 56.dp
    val paletteColors = if (isDarkMode) palette.darkColors else palette.lightColors
    val backgroundColor = if (isDarkMode) Color(0xFF040E1E) else Color.White

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showIconHeader) {
            Image(
                modifier = Modifier.size(17.dp),
                painter = painterResource(
                    if (isDarkMode) {
                        R.drawable.ic_moon
                    } else {
                        R.drawable.ic_sun
                    }
                ),
                contentDescription = null
            )
            Spacer(Modifier.height(15.dp))
        }

        Text(
            text = palette.asTitle(),
            style = Typography.labelSmall.copy(
                fontSize = 11.sp,
                color = if (isDarkMode) {
                    Color.White
                } else {
                    WarmBrown
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .size(paletteSize),
            shape = RoundedCornerShape(0),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(.5f)
                        .align(Alignment.TopStart)
                        .padding(end = .25.dp, bottom = .25.dp)
                        .background(paletteColors.primary)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(.5f)
                            .align(Alignment.Center)
                            .background(paletteColors.onPrimary)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(.5f)
                        .align(Alignment.TopEnd)
                        .padding(start = .25.dp, bottom = .25.dp)
                        .background(paletteColors.background)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(.5f)
                            .align(Alignment.Center)
                            .background(paletteColors.onBackground)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(.5f)
                        .align(Alignment.BottomStart)
                        .padding(end = .25.dp, top = .25.dp)
                        .background(paletteColors.accent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(.5f)
                            .align(Alignment.Center)
                            .background(paletteColors.onAccent)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(.5f)
                        .align(Alignment.BottomEnd)
                        .padding(start = .25.dp, top = .25.dp)
                        .background(paletteColors.illustration)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteSelection(initiallyExpanded = true, selectionClickPalette = {})
}

@Composable
private fun ReccoPalette.asTitle() = stringResource(
    when (this) {
        is ReccoPalette.Custom -> TODO()
        ReccoPalette.Fresh -> R.string.fresh
        ReccoPalette.Ocean -> R.string.ocean
    }
)

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
