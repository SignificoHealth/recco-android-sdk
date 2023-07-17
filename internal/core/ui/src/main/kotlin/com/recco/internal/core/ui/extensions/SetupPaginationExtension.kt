package com.recco.internal.core.ui.extensions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.recco.internal.core.ui.components.AppPrimaryButton
import com.recco.internal.core.ui.components.AppProgressLoadingCircled
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
private fun ErrorStateViewContent(
    throwable: Throwable? = null,
    callToActionButtonListener: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.dp_16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = throwable.asDescriptionRes()),
            style = AppTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(AppSpacing.dp_16))

        AppPrimaryButton(
            onClick = { callToActionButtonListener() },
            textRes = throwable.asCtaTextRes(),
            iconStartRes = throwable.asCtaIconRes(),
        )
    }
}

@Composable
private fun LoadingStateViewContent(isVerticalList: Boolean) {
    val topPadding = if (isVerticalList) AppSpacing.dp_16 else 0.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(topPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppProgressLoadingCircled()
    }
}
