package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.metric.AppUserMetricAction
import com.recco.internal.core.model.metric.AppUserMetricCategory
import com.recco.internal.core.model.metric.AppUserMetricEvent
import com.recco.internal.core.openapi.model.AppUserMetricActionDTO
import com.recco.internal.core.openapi.model.AppUserMetricCategoryDTO
import com.recco.internal.core.openapi.model.AppUserMetricEventDTO

internal fun AppUserMetricAction.asDTO() = when (this) {
    AppUserMetricAction.VIEW -> AppUserMetricActionDTO.VIEW
    AppUserMetricAction.HOST_APP_OPEN -> AppUserMetricActionDTO.HOST_APP_OPEN
    AppUserMetricAction.RECCO_SDK_OPEN -> AppUserMetricActionDTO.RECCO_SDK_OPEN
}

internal fun AppUserMetricCategory.asDTO() = when (this) {
    AppUserMetricCategory.USER_SESSION -> AppUserMetricCategoryDTO.USER_SESSION
    AppUserMetricCategory.DASHBOARD_SCREEN -> AppUserMetricCategoryDTO.DASHBOARD_SCREEN
}

internal fun AppUserMetricEvent.asDTO() = AppUserMetricEventDTO(
    category = this.category.asDTO(),
    action = this.action.asDTO(),
    value = this.valueEvent
)
