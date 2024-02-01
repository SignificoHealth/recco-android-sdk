package com.recco.internal.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@Composable
fun RecommendationTypeRow(
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    textStyle: TextStyle = AppTheme.typography.labelSmall,
    contentType: ContentType,
    lengthInSeconds: Int?,
    iconSpacing: Dp = AppSpacing.dp_8
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(iconSpacing),
        modifier = modifier
    ) {
        val (iconDrawable, contentTypeString) = when (contentType) {
            ContentType.ARTICLE -> {
                R.drawable.recco_ic_document to
                    R.string.recco_reccomendation_type_read
            }
            ContentType.AUDIO -> {
                R.drawable.recco_ic_sound to
                    R.string.recco_reccomendation_type_podcast
            }
            ContentType.VIDEO -> {
                R.drawable.recco_ic_video to
                    R.string.recco_reccomendation_type_video
            }
            else -> { null to null }
        }

        if (iconDrawable != null) {
            Icon(
                painter = painterResource(id = iconDrawable),
                tint = AppTheme.colors.primary,
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
        }

        if (contentTypeString != null) {
            val contentTypeText = stringResource(id = contentTypeString)
            val minSuffixText = stringResource(id = R.string.recco_unit_min)
            val contentTypeDurationText = buildString {
                append(contentTypeText)

                if (lengthInSeconds != null) {
                    val lengthInMinutes = (lengthInSeconds / 60)
                    append("  • $lengthInMinutes $minSuffixText").takeIf { lengthInSeconds != null }
                }
            }

            Text(
                text = contentTypeDurationText,
                style = textStyle.copy(color = AppTheme.colors.primary)
            )
        }
    }
}

@Preview
@Composable
fun RecommendationTypeRowPreview() {
    AppTheme {
        Surface {
            Column(
                modifier = Modifier.padding(AppSpacing.dp_16),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.dp_16)
            ) {
                ContentType.entries.forEach {
                    RecommendationTypeRow(
                        contentType = it,
                        lengthInSeconds = (it.ordinal + 1) * 60
                    )
                }
            }
        }
    }
}
