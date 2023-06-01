package com.shadowflight.uicommons.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.shadowflight.uicommons.theme.AppTheme

@Composable
fun AppStatusBar(
    color: Color = AppTheme.colors.primary,
    darkIcons: Boolean = false
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = darkIcons
        )
    }
}