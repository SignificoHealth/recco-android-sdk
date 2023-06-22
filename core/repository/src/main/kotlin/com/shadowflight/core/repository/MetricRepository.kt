package com.shadowflight.core.repository

import com.shadowflight.core.base.di.ApplicationScope
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.openapi.api.MetricApi
import com.shadowflight.core.openapi.model.AppUserMetricActionDTO
import com.shadowflight.core.openapi.model.AppUserMetricCategoryDTO
import com.shadowflight.core.openapi.model.AppUserMetricEventDTO
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