package com.shadowflight.core.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.shadowflight.core.ui.R
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.FeedSectionType.*

@Composable
fun FeedSectionType.asSectionTitle(): String = when (this) {
    PHYSICAL_ACTIVITY_RECOMMENDATIONS -> stringResource(
        R.string.recommended_for_you_topic,
        stringResource(R.string.physical_activity)
    )

    NUTRITION_RECOMMENDATIONS -> stringResource(
        R.string.recommended_for_you_topic,
        stringResource(R.string.nutrition)
    )

    MENTAL_WELLBEING_RECOMMENDATIONS -> stringResource(
        R.string.recommended_for_you_topic,
        stringResource(R.string.mental_wellbeing)
    )

    SLEEP_RECOMMENDATIONS -> stringResource(
        R.string.recommended_for_you_topic,
        stringResource(R.string.sleep)
    )

    PHYSICAL_ACTIVITY_EXPLORE -> stringResource(
        R.string.explore_topic,
        stringResource(R.string.physical_activity)
    )

    NUTRITION_EXPLORE -> stringResource(
        R.string.explore_topic,
        stringResource(R.string.nutrition)
    )

    MENTAL_WELLBEING_EXPLORE -> stringResource(
        R.string.explore_topic,
        stringResource(R.string.mental_wellbeing)
    )

    SLEEP_EXPLORE -> stringResource(
        R.string.explore_topic,
        stringResource(R.string.sleep)
    )

    STARTING_RECOMMENDATIONS -> stringResource(R.string.start_here)

    PREFERRED_RECOMMENDATIONS -> stringResource(R.string.you_may_also_like)

    MOST_POPULAR -> stringResource(R.string.trending)

    NEW_CONTENT -> stringResource(R.string.news_for_your)
}