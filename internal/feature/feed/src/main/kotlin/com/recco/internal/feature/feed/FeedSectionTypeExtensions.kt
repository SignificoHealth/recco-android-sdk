package com.recco.internal.feature.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.FeedSectionType.MENTAL_WELLBEING_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.MOST_POPULAR
import com.recco.internal.core.model.feed.FeedSectionType.NEW_CONTENT
import com.recco.internal.core.model.feed.FeedSectionType.NUTRITION_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.NUTRITION_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.PREFERRED_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.SLEEP_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.SLEEP_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.STARTING_RECOMMENDATIONS
import com.recco.internal.core.ui.R

@Composable
internal fun FeedSectionType.asSectionTitle(): String = when (this) {
    PHYSICAL_ACTIVITY_RECOMMENDATIONS -> {
        stringResource(
            R.string.recco_dashboard_recommended_for_you_topic,
            stringResource(R.string.recco_dashboard_alert_physical_activity_title)
        )
    }

    NUTRITION_RECOMMENDATIONS -> stringResource(
        R.string.recco_dashboard_recommended_for_you_topic,
        stringResource(R.string.recco_dashboard_alert_nutrition_title)
    )

    MENTAL_WELLBEING_RECOMMENDATIONS -> stringResource(
        R.string.recco_dashboard_recommended_for_you_topic,
        stringResource(R.string.recco_dashboard_alert_mental_wellbeing_title)
    )

    SLEEP_RECOMMENDATIONS -> stringResource(
        R.string.recco_dashboard_recommended_for_you_topic,
        stringResource(R.string.recco_dashboard_alert_sleep_title)
    )

    PHYSICAL_ACTIVITY_EXPLORE, NUTRITION_EXPLORE, MENTAL_WELLBEING_EXPLORE, SLEEP_EXPLORE -> stringResource(
        R.string.recco_dashboard_explore_topic
    )

    STARTING_RECOMMENDATIONS -> stringResource(R.string.recco_dashboard_start_here)

    PREFERRED_RECOMMENDATIONS -> stringResource(R.string.recco_dashboard_you_may_also_like)

    MOST_POPULAR -> stringResource(R.string.recco_dashboard_trending)

    NEW_CONTENT -> stringResource(R.string.recco_dashboard_new_for_you)
}