package com.recco.internal.feature.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.recco.internal.core.ui.R
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.FeedSectionType.*

@Composable
fun FeedSectionType.asSectionTitle(): String = when (this) {
    PHYSICAL_ACTIVITY_RECOMMENDATIONS -> stringResource(
        R.string.recco_recommended_for_you_topic,
        stringResource(R.string.recco_physical_activity)
    )

    NUTRITION_RECOMMENDATIONS -> stringResource(
        R.string.recco_recommended_for_you_topic,
        stringResource(R.string.recco_nutrition)
    )

    MENTAL_WELLBEING_RECOMMENDATIONS -> stringResource(
        R.string.recco_recommended_for_you_topic,
        stringResource(R.string.recco_mental_wellbeing)
    )

    SLEEP_RECOMMENDATIONS -> stringResource(
        R.string.recco_recommended_for_you_topic,
        stringResource(R.string.recco_sleep)
    )

    PHYSICAL_ACTIVITY_EXPLORE -> stringResource(
        R.string.recco_explore_topic,
        stringResource(R.string.recco_physical_activity)
    )

    NUTRITION_EXPLORE -> stringResource(
        R.string.recco_explore_topic,
        stringResource(R.string.recco_nutrition)
    )

    MENTAL_WELLBEING_EXPLORE -> stringResource(
        R.string.recco_explore_topic,
        stringResource(R.string.recco_mental_wellbeing)
    )

    SLEEP_EXPLORE -> stringResource(
        R.string.recco_explore_topic,
        stringResource(R.string.recco_sleep)
    )

    STARTING_RECOMMENDATIONS -> stringResource(R.string.recco_start_here)

    PREFERRED_RECOMMENDATIONS -> stringResource(R.string.recco_you_may_also_like)

    MOST_POPULAR -> stringResource(R.string.recco_trending)

    NEW_CONTENT -> stringResource(R.string.recco_news_for_your)
}