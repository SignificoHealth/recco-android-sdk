package com.shadowflight.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shadowflight.uicommons.theme.AppSpacing
import com.shadowflight.uicommons.theme.AppTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FeedRoute(
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.getMyId().collectAsStateWithLifecycle(initialValue = FeedViewUIState())
    FeedScreen(uiState)
}

@Composable
fun FeedScreen(
    uiState: FeedViewUIState
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.dp_16)
    ) {
        Text(text = "FeedScreen, userId: ${uiState.userId}", style = AppTheme.typography.body1)
    }
}