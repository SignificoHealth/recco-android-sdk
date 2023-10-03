package com.recco.internal.core.model.metric

data class AppUserMetricEvent(
    val category: AppUserMetricCategory,
    val action: AppUserMetricAction,
    val `value`: String? = null
)
