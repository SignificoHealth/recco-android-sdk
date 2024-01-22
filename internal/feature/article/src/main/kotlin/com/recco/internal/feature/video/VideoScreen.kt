package com.recco.internal.feature.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun VideoRoute(
    navigateUp: () -> Unit,
    viewModel: VideoViewModel = hiltViewModel()
) {
    VideoScreen()
}

@Composable
private fun VideoScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red))
}
