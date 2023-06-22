package com.recco.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.recco.core.model.feed.Topic
import com.recco.core.ui.R

@Composable
fun Topic.asTitle() = stringResource(asResTitle())

fun Topic.asResTitle() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> R.string.recco_physical_activity
    Topic.NUTRITION -> R.string.recco_nutrition
    Topic.MENTAL_WELLBEING -> R.string.recco_mental_wellbeing
    Topic.SLEEP -> R.string.recco_sleep
}

fun Topic.asResExplanation() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> R.string.recco_physical_activity_explanation
    Topic.NUTRITION -> R.string.recco_nutrition_explanation
    Topic.MENTAL_WELLBEING -> R.string.recco_mental_wellbeing_explanation
    Topic.SLEEP -> R.string.recco_sleep_explanation
}