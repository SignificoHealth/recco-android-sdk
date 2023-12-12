package com.recco.internal.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.extensions.asResTitle
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppQuestionnaireCard(
    topic: Topic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(heightRecommendationCard)
            .width(widthRecommendationCard),
        elevation = AppTheme.elevation.card,
        onClick = onClick,
        backgroundColor = AppTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .background(AppTheme.colors.accent20)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppSpacing.dp_12)
                        .align(Alignment.Center)
                        // To counter balance the source padding from Image below
                        .offset(y = AppSpacing.dp_12),
                    text = stringResource(
                        R.string.recco_dashboard_recommended_questionnaire_topic,
                        stringResource(topic.asResTitle())
                    ),
                    style = AppTheme.typography.body2Bold,
                    textAlign = TextAlign.Center
                )
            }

            AppTintedImagePeopleDigital(
                modifier = Modifier.width(widthRecommendationCard)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme(darkTheme = false) {
        AppQuestionnaireCard(
            topic = Topic.MENTAL_WELLBEING,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AppQuestionnaireCard(
            topic = Topic.MENTAL_WELLBEING,
            onClick = {}
        )
    }
}
