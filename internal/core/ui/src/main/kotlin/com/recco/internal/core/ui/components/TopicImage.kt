package com.recco.internal.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.feed.Topic.MENTAL_WELLBEING
import com.recco.internal.core.model.feed.Topic.NUTRITION
import com.recco.internal.core.model.feed.Topic.PHYSICAL_ACTIVITY
import com.recco.internal.core.model.feed.Topic.SLEEP

@Composable
fun TopicImage(
    topic: Topic,
    modifier: Modifier
) {
    when (topic) {
        PHYSICAL_ACTIVITY -> AppTintedImageActivity(modifier = modifier)
        NUTRITION -> AppTintedImageEating(modifier = modifier)
        MENTAL_WELLBEING -> AppTintedImagePeopleDigital(modifier = modifier)
        SLEEP -> AppTintedImageSleep(modifier = modifier)
    }
}
