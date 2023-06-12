@file:OptIn(ExperimentalAnimationApi::class)

package com.shadowflight.core.questionnaire

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.components.AppPrimaryButton
import com.shadowflight.core.ui.components.AppSecondaryButton
import com.shadowflight.core.ui.theme.AppSpacing

@Composable
fun QuestionnaireFooterContent(
    modifier: Modifier = Modifier,
    showBack: Boolean,
    nextEnabled: Boolean,
    isLastPage: Boolean,
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
                    iconStartRes = R.drawable.ic_chevron_back,
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
            text = stringResource(if (isLastPage) R.string.finish else R.string.next)
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
        isQuestionnaireSubmitLoading = false,
        onBackClicked = { },
        onNextClicked = {})
}

@Preview
@Composable
private fun PreviewLastPage() {
    QuestionnaireFooterContent(
        modifier = Modifier,
        showBack = true,
        nextEnabled = true,
        isLastPage = true,
        isQuestionnaireSubmitLoading = false,
        onBackClicked = { },
        onNextClicked = {})
}