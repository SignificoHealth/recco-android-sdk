package com.recco.core.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.recco.core.ui.extensions.asCtaIconRes
import com.recco.core.ui.extensions.asCtaTextRes
import com.recco.core.ui.extensions.asDescriptionRes
import com.recco.core.ui.extensions.asDrawableComposable
import com.recco.core.ui.extensions.asDrawableRes
import com.recco.core.ui.extensions.asTitleRes

@Composable
internal fun AppErrorContent(
    throwable: Throwable? = null,
    retry: () -> Unit,
    scrollState: ScrollState? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .let {
                scrollState?.let { state ->
                    it.verticalScroll(state)
                } ?: it
            },
        verticalArrangement = Arrangement.Center
    ) {
        AppEmptyContent(
            emptyState = EmptyState(
                titleRes = throwable.asTitleRes(),
                description = stringResource(id = throwable.asDescriptionRes()),
                drawableRes = throwable.asDrawableRes(),
                drawableComposable = throwable.asDrawableComposable(),
                ctaIconRes = throwable.asCtaIconRes(),
                ctaTextRes = throwable.asCtaTextRes(),
                onCtaClick = retry
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun Preview() {
    AppErrorContent(
        retry = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewNetworkError() {
    AppErrorContent(
        throwable = RuntimeException(),
        retry = {},
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewNoBack() {
    AppErrorContent(
        retry = {}
    )
}
