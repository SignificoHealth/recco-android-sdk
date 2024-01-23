package com.recco.internal.feature.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.BackIconButton
import com.recco.internal.core.ui.components.PlayerSlider
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
internal fun VideoRoute(
    navigateUp: () -> Unit,
    viewModel: VideoViewModel = hiltViewModel()
) {
    VideoScreen(navigateUp)
}

@Composable
private fun VideoScreen(
    navigateUp: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSpacing.dp_16),
            modifier = Modifier.fillMaxWidth()
        ) {
            AppTopBar(
                title = null,
                elevation = 0.dp,
                navigationIcon = {
                    BackIconButton(
                        onClick = navigateUp,
                        iconTint = Color.White
                    )
                },
                backgroundColor = Color.Transparent
            )

            Text(
                text = "Sleeping like a baby",
                style = AppTheme.typography.h1.copy(color = Color.White),
            )

            Text(
                text = "Audio • 1-5min",
                style = AppTheme.typography.labelSmall.copy(color = Color.White),
            )
        }

        FloatingActionButton(
            onClick = {},
            backgroundColor = AppTheme.colors.accent,
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.Center),
        ) {

            Icon(
                painter = painterResource(id = com.recco.internal.core.ui.R.drawable.recco_ic_play),
                tint = Color.White,
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(AppSpacing.dp_16)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_24)) {
                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                        ,
                        painter = painterResource(
                            R.drawable.recco_ic_airplay
                        ),
                        contentDescription = null,
                        tint = Color.White
                    )

                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                        ,
                        painter = painterResource(
                            R.drawable.recco_ic_captions
                        ),
                        contentDescription = null,
                        tint = Color.White
                    )

                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                        ,
                        painter = painterResource(
                            R.drawable.recco_ic_settings
                        ),
                        contentDescription = null,
                        tint = Color.White
                    )

                }

                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.dp_24)) {
                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                        ,
                        painter = painterResource(
                            R.drawable.recco_ic_thumbs_up_filled
                        ),
                        contentDescription = null,
                        tint = AppTheme.colors.accent                    )

                    Icon(
                        modifier = Modifier
                            .size(AppSpacing.dp_24)
                        ,
                        painter = painterResource(
                            R.drawable.recco_ic_thumbs_down_outline
                        ),
                        contentDescription = null,
                        tint = Color.White                    )

                }

            }

            PlayerSlider(
                audioDuration = 25_000,
                currentPosition = 10,
                onSeekPosition = {},
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "2:53",
                    style = AppTheme.typography.labelSmall.copy(color = Color.White),
                )
                Text(
                    text = "2:53",
                    style = AppTheme.typography.labelSmall.copy(color = Color.White),
                )
            }
        }
    }
}



@Preview
@Composable
private fun VideoScreenPreview() {
    AppTheme {
        VideoScreen(navigateUp = {})
    }
}
