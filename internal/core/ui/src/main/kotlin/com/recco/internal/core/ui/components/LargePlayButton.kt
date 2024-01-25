package com.recco.internal.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
fun LargePlayButton(
    modifier: Modifier = Modifier,
    onPLayButtonClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = Color.DarkGray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(AppSpacing.dp_8)
    ) {
        IconButton(
            onClick = onPLayButtonClick,
            modifier = Modifier
                .background(
                    color = AppTheme.colors.accent,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(
                    horizontal = AppSpacing.dp_24,
                    vertical = AppSpacing.dp_8
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.recco_ic_play),
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = stringResource(id = R.string.recco_play),
            style = AppTheme.typography.h3,
            color = Color.White,
            modifier = Modifier.padding(horizontal = AppSpacing.dp_24)
        )
    }
}

@Preview
@Composable
fun LargePlayButtonPreview() {
    AppTheme {
        Box(modifier = Modifier
            .background(color = Color.LightGray)
            .padding(AppSpacing.dp_40)
        ) {
            LargePlayButton {

            }
        }
    }
}
