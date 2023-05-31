package com.shadowflight.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.shadowflight.ui.R
import com.shadowflight.model.feed.FeedSectionType
import com.shadowflight.model.feed.FeedSectionType.*

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

    PHYSICAL_WELLBEING_RECOMMENDATIONS -> stringResource(
        R.string.recommended_for_you_topic,
        stringResource(R.string.physical_wellbeing)
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

    PHYSICAL_WELLBEING_EXPLORE -> stringResource(
        R.string.explore_topic,
        stringResource(R.string.physical_wellbeing)
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