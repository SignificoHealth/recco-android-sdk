package com.shadowflight.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shadowflight.uicommons.theme.AppSpacing
import com.shadowflight.uicommons.theme.AppTheme

@Composable
fun FeedRoute() {
    FeedScreen()
}

@Composable
fun FeedScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.dp_16)
    ) {
        Text(text = "FeedScreen", style = AppTheme.typography.body1)
    }
}