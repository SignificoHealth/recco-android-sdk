package com.shadowflight.core.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shadowflight.core.ui.components.AppPrimaryButton
import com.shadowflight.core.ui.theme.AppSpacing

@Composable
fun OnboardingRoute(navigateToQuestionnaire: () -> Unit) {
    OnboardingScreen(navigateToQuestionnaire)
}

@Composable
fun OnboardingScreen(navigateToQuestionnaire: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.dp_24)
    ) {
        AppPrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = navigateToQuestionnaire,
            text = "Navigate To questionnaire"
        )
    }
}