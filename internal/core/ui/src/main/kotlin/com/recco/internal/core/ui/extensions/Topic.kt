package com.recco.internal.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.ui.R

@Composable
fun Topic.asTitle() = stringResource(asResTitle())

fun Topic.asResTitle() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> R.string.recco_dashboard_alert_physical_activity_title
    Topic.NUTRITION -> R.string.recco_dashboard_alert_nutrition_title
    Topic.MENTAL_WELLBEING -> R.string.recco_dashboard_alert_mental_wellbeing_title
    Topic.SLEEP -> R.string.recco_dashboard_alert_sleep_title
}

fun Topic.asResExplanation() = when (this) {
    Topic.PHYSICAL_ACTIVITY -> R.string.recco_dashboard_alert_physical_activity_body
    Topic.NUTRITION -> R.string.recco_dashboard_alert_nutrition_body
    Topic.MENTAL_WELLBEING -> R.string.recco_dashboard_alert_mental_wellbeing_body
    Topic.SLEEP -> R.string.recco_dashboard_alert_sleep_body
}
