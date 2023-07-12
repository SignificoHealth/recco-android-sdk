@file:OptIn(ExperimentalAnimationApi::class)

package com.recco.internal.feature.questionnaire

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppPrimaryButton
import com.recco.internal.core.ui.components.AppSecondaryButton
import com.recco.internal.core.ui.theme.AppSpacing

@Composable
fun QuestionnaireFooterContent(
    modifier: Modifier = Modifier,
    showBack: Boolean,
    nextEnabled: Boolean,
    isLastPage: Boolean,
    isOnboarding: Boolean,
    isQuestionnaireSubmitLoading: Boolean,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(
            visible = showBack,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Row {
                AppSecondaryButton(
                    iconStartRes = R.drawable.recco_ic_chevron_back,
                    onClick = onBackClicked,
                    text = null,
                    enabled = !isQuestionnaireSubmitLoading
                )
                Spacer(Modifier.width(AppSpacing.dp_8))
            }
        }

        AppPrimaryButton(
            modifier = Modifier.weight(1f),
            enabled = nextEnabled,
            onClick = onNextClicked,
            isLoading = isQuestionnaireSubmitLoading,
            text = stringResource(if (isLastPage && !isOnboarding) R.string.recco_finish_button else R.string.recco_continue_button)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    QuestionnaireFooterContent(
        modifier = Modifier,
        showBack = true,
        nextEnabled = true,
        isLastPage = false,
        isOnboarding = false,
        isQuestionnaireSubmitLoading = false,
        onBackClicked = { },
        onNextClicked = {}
    )
}

@Preview
@Composable
private fun PreviewSubmitting() {
    QuestionnaireFooterContent(
        modifier = Modifier,
        showBack = true,
        nextEnabled = true,
        isLastPage = true,
        isOnboarding = false,
        isQuestionnaireSubmitLoading = true,
        onBackClicked = { },
        onNextClicked = {}
    )
}


@Preview
@Composable
private fun PreviewNoBack() {
    QuestionnaireFooterContent(
        modifier = Modifier,
        showBack = false,
        nextEnabled = true,
        isLastPage = false,
        isOnboarding = false,
        isQuestionnaireSubmitLoading = false,
        onBackClicked = { },
        onNextClicked = {}
    )
}

@Preview
@Composable
private fun PreviewLastPage() {
    QuestionnaireFooterContent(
        modifier = Modifier,
        showBack = true,
        nextEnabled = true,
        isLastPage = true,
        isOnboarding = false,
        isQuestionnaireSubmitLoading = false,
        onBackClicked = { },
        onNextClicked = {}
    )
}

@Preview
@Composable
private fun PreviewLastOnboardingPage() {
    QuestionnaireFooterContent(
        modifier = Modifier,
        showBack = true,
        nextEnabled = true,
        isLastPage = true,
        isOnboarding = true,
        isQuestionnaireSubmitLoading = false,
        onBackClicked = { },
        onNextClicked = {}
    )
}