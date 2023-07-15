package com.recco.internal.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

/**
 * [drawableRes] will take precedence over [drawableComposable]
 */
data class EmptyState(
    @StringRes val titleRes: Int,
    val description: String? = null,
    @DrawableRes val drawableRes: Int? = null,
    val drawableComposable: @Composable (() -> Unit)? = null,
    @StringRes val ctaTextRes: Int? = null,
    @DrawableRes val ctaIconRes: Int? = null,
    val onCtaClick: (() -> Unit)? = null
)

@Composable
fun AppEmptyContent(
    drawableModifier: Modifier = Modifier,
    emptyState: EmptyState,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.dp_24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (emptyState.drawableRes != null) {
                Image(
                    modifier = drawableModifier,
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = emptyState.drawableRes),
                    contentDescription = null
                )
                Spacer(Modifier.height(AppSpacing.dp_40))
            } else if (emptyState.drawableComposable != null) {
                emptyState.drawableComposable.invoke()
                Spacer(Modifier.height(AppSpacing.dp_40))
            }

            Text(
                text = stringResource(emptyState.titleRes),
                style = AppTheme.typography.h1,
                textAlign = TextAlign.Center
            )

            emptyState.description?.let { description ->
                Spacer(Modifier.height(AppSpacing.dp_8))

                Text(
                    text = description,
                    style = AppTheme.typography.body2,
                    textAlign = TextAlign.Center
                )
            }

            emptyState.onCtaClick?.let {
                Spacer(Modifier.height(AppSpacing.dp_8))
                Spacer(Modifier.height(AppSpacing.dp_40))

                AppPrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    textRes = emptyState.ctaTextRes,
                    iconStartRes = emptyState.ctaIconRes,
                    onClick = emptyState.onCtaClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewWithoutCta() {
    AppEmptyContent(
        emptyState = EmptyState(
            titleRes = R.string.recco_error_connection_title,
            description = stringResource(R.string.recco_error_connection_body),
            drawableRes = R.drawable.recco_ic_no_connection,
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewWithoutDescription() {
    AppEmptyContent(
        emptyState = EmptyState(
            titleRes = R.string.recco_error_connection_title,
            drawableRes = R.drawable.recco_ic_no_connection,
            ctaTextRes = R.string.recco_error_reload,
            ctaIconRes = R.drawable.recco_ic_retry,
            onCtaClick = { }
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewFull() {
    AppEmptyContent(
        emptyState = EmptyState(
            titleRes = R.string.recco_error_connection_title,
            description = stringResource(R.string.recco_error_connection_body),
            drawableRes = R.drawable.recco_ic_no_connection,
            ctaTextRes = R.string.recco_error_reload,
            ctaIconRes = R.drawable.recco_ic_retry,
            onCtaClick = { }
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewCustomImageSize() {
    AppEmptyContent(
        drawableModifier = Modifier
            .size(145.dp)
            .aspectRatio(4 / 3f),
        emptyState = EmptyState(
            titleRes = R.string.recco_error_connection_title,
            description = stringResource(R.string.recco_error_connection_body),
            drawableRes = null,
            drawableComposable = {
                AppTintedImageNoConnection(tint = AppTheme.colors.primary)
            },
            ctaTextRes = R.string.recco_error_reload,
            ctaIconRes = R.drawable.recco_ic_retry,
            onCtaClick = { }
        ),
    )
}
