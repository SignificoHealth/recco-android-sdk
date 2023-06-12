package com.shadowflight.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.ui.R

@Composable
fun Topic.asTitle() = stringResource(
    when (this) {
        Topic.PHYSICAL_ACTIVITY -> R.string.physical_activity
        Topic.NUTRITION -> R.string.nutrition
        Topic.PHYSICAL_WELLBEING -> R.string.physical_wellbeing
        Topic.SLEEP -> R.string.sleep
    }
)