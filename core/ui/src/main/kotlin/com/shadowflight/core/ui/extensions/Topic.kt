package com.shadowflight.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.ui.R

@Composable
fun Topic.asTitle() = stringResource(asResTitle())

fun Topic.asResTitle() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> R.string.physical_activity
    Topic.NUTRITION -> R.string.nutrition
    Topic.MENTAL_WELLBEING -> R.string.mental_wellbeing
    Topic.SLEEP -> R.string.sleep
}

fun Topic.asResExplanation() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> R.string.physical_activity_explanation
    Topic.NUTRITION -> R.string.nutrition_explanation
    Topic.MENTAL_WELLBEING -> R.string.mental_wellbeing_explanation
    Topic.SLEEP -> R.string.sleep_explanation
}