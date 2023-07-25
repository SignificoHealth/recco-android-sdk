package com.recco.internal.feature.questionnaire

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.ASPECT_RATIO_1_1
import com.recco.internal.core.ui.components.AppPrimaryButton
import com.recco.internal.core.ui.components.AppTintedImagePortrait
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.CloseIconButton
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
internal fun QuestionnaireOnboardingOutroRoute(
    navigateToFeed: () -> Unit
) {
    QuestionnaireOnboardingOutroScreen(
        navigateToOutro = navigateToFeed
    )
}

@Composable
private fun QuestionnaireOnboardingOutroScreen(
    modifier: Modifier = Modifier,
    navigateToOutro: () -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    BackHandler(
        enabled = true,
        onBack = { navigateToOutro() }
    )

    Scaffold(
        topBar = {
            AppTopBar(
                actions = {
                    CloseIconButton(
                        onClick = { navigateToOutro() }
                    )
                }
            )
        },
        bottomBar = {
            AppPrimaryButton(
                text = stringResource(R.string.recco_questionnaire_go_to_dashboard),
                onClick = navigateToOutro,
                modifier = modifier
                    .padding(horizontal = AppSpacing.dp_24)
                    .padding(bottom = AppSpacing.dp_24)
                    .fillMaxWidth()
            )
        }
    ) {
        Column(
            modifier = modifier
                .background(AppTheme.colors.background)
                .fillMaxSize()
                .fillMaxWidth()
                .padding(top = contentPadding.calculateTopPadding())
                .padding(bottom = contentPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppTintedImagePortrait(
                Modifier
                    .background(AppTheme.colors.accent20)
                    .padding(top = AppSpacing.dp_32)
                    .padding(horizontal = AppSpacing.dp_32 * 2)
                    .aspectRatio(ratio = ASPECT_RATIO_1_1)
            )

            Spacer(modifier = Modifier.height(AppSpacing.dp_40))

            Text(
                text = stringResource(id = R.string.recco_onboarding_outro_title),
                modifier = modifier
                    .padding(horizontal = AppSpacing.dp_24),
                color = AppTheme.colors.primary,
                textAlign = TextAlign.Center,
                style = AppTheme.typography.h1
            )

            Spacer(modifier = Modifier.height(AppSpacing.dp_24))

            Text(
                text = stringResource(id = R.string.recco_onboarding_outro_body),
                modifier = modifier
                    .padding(horizontal = AppSpacing.dp_24)
                    .padding(bottom = AppSpacing.dp_12),
                color = AppTheme.colors.primary,
                textAlign = TextAlign.Center,
                style = AppTheme.typography.body2
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        QuestionnaireOnboardingOutroScreen(
            navigateToOutro = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        QuestionnaireOnboardingOutroScreen(
            navigateToOutro = {}
        )
    }
}
