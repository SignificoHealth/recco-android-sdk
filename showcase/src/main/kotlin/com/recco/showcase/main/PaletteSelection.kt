package com.recco.showcase.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recco.api.model.ReccoPalette
import com.recco.showcase.R
import com.recco.showcase.data.mappers.FRESH_PALETTE_ID
import com.recco.showcase.data.mappers.asShowcasePalette
import com.recco.showcase.models.ShowcasePalette
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun PaletteSelection(
    palettes: List<ShowcasePalette>,
    selectionClickPalette: (ShowcasePalette) -> Unit,
    createCustomPalette: () -> Unit,
    editCustomPalette: (ShowcasePalette) -> Unit,
    initiallyExpanded: Boolean = false,
    selectedPaletteId: Int
) {
    val expanded = remember { mutableStateOf(initiallyExpanded) }

    CustomDropdown(
        expandedState = expanded,
        iconRes = R.drawable.ic_palette
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            palettes
                .forEachIndexed { index, palette ->
                    MosaicSampleSection(
                        palette,
                        showIconHeader = index == 0,
                        selectionClickStyle = {
                            expanded.value = false
                            selectionClickPalette(it)
                        },
                        selectedPalette = selectedPaletteId == palette.id
                    )
                }

            Divider()
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(70.dp)
                    .clickable { createCustomPalette() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(WarmBrown)
                        .padding(5.dp),
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.new_palette),
                    style = Typography.labelLarge.copy(color = WarmBrown)
                )
            }
        }
    }
}

@Composable
fun MosaicSampleSection(
    palette: ShowcasePalette,
    showIconHeader: Boolean,
    selectionClickStyle: (ShowcasePalette) -> Unit,
    selectedPalette: Boolean
) {
    Row(
        Modifier
            .noRippleClickable { selectionClickStyle(palette) }
    ) {
        MosaicSample(
            palette = palette,
            showIconHeader = showIconHeader,
            isDarkMode = false,
            selectedPalette = selectedPalette
        )

        MosaicSample(
            palette = palette,
            showIconHeader = showIconHeader,
            isDarkMode = true,
            selectedPalette = selectedPalette
        )
    }
}

@Composable
fun MosaicSample(
    palette: ShowcasePalette,
    showIconHeader: Boolean,
    isDarkMode: Boolean,
    selectedPalette: Boolean
) {
    val styleSize = 56.dp
    val styleColors = if (isDarkMode) palette.darkColors else palette.lightColors
    val backgroundColor = if (isDarkMode) Color(0xFF040E1E) else Color.White

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showIconHeader) {
            Spacer(Modifier.height(15.dp))
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

        Column(
            Modifier
                .fillMaxWidth()
                .let {
                    if (selectedPalette) {
                        it.border(2.dp, SoftYellow)
                    } else {
                        it
                    }
                }
                .padding(10.dp)
        ) {
            Text(
                text = palette.name,
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
                    .size(styleSize),
                shape = RoundedCornerShape(0),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                            .background(styleColors.primary.asComposeColor())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(.5f)
                                .align(Alignment.Center)
                                .background(styleColors.onPrimary.asComposeColor())
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(.5f)
                            .align(Alignment.TopEnd)
                            .padding(start = .25.dp, bottom = .25.dp)
                            .background(styleColors.background.asComposeColor())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(.5f)
                                .align(Alignment.Center)
                                .background(styleColors.onBackground.asComposeColor())
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(.5f)
                            .align(Alignment.BottomStart)
                            .padding(end = .25.dp, top = .25.dp)
                            .background(styleColors.accent.asComposeColor())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(.5f)
                                .align(Alignment.Center)
                                .background(styleColors.onAccent.asComposeColor())
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(.5f)
                            .align(Alignment.BottomEnd)
                            .padding(start = .25.dp, top = .25.dp)
                            .background(styleColors.illustration.asComposeColor())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(.5f)
                                .align(Alignment.Center)
                                .background(styleColors.illustrationOutline.asComposeColor())
                        )
                    }
                }
            }
        }
    }
}

private fun String.asComposeColor() = Color(android.graphics.Color.parseColor(this))

private val palettesPreview = listOf(
    ReccoPalette.Fresh,
    ReccoPalette.Ocean,
    ReccoPalette.Spring,
    ReccoPalette.Tech
).map { it.asShowcasePalette() }

@Preview
@Composable
private fun Preview() {
    PaletteSelection(
        palettes = palettesPreview,
        initiallyExpanded = true,
        selectionClickPalette = {},
        selectedPaletteId = FRESH_PALETTE_ID,
        createCustomPalette = {},
        editCustomPalette = {}
    )
}
