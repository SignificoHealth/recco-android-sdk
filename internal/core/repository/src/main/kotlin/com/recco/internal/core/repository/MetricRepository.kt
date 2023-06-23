package com.recco.internal.core.repository

import com.recco.internal.core.base.di.ApplicationScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.openapi.api.MetricApi
import com.recco.internal.core.openapi.model.AppUserMetricActionDTO
import com.recco.internal.core.openapi.model.AppUserMetricCategoryDTO
import com.recco.internal.core.openapi.model.AppUserMetricEventDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetricRepository @Inject constructor(
    private val metricApi: MetricApi,
    private val logger: Logger,
    @ApplicationScope private val appScope: CoroutineScope,
) {

    fun openDashboard() {
        appScope.launch {
            runCatching {
                metricApi.logEvent(
                    AppUserMetricEventDTO(
                        category = AppUserMetricCategoryDTO.DASHBOARD_SCREEN,
                        action = AppUserMetricActionDTO.VIEW
                    )
                )
            }.onFailure { logger.e(it) }
        }
    }
}