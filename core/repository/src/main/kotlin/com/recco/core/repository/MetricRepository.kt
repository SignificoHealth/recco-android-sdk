package com.recco.core.repository

import com.recco.core.base.di.ApplicationScope
import com.recco.core.logger.Logger
import com.recco.core.openapi.api.MetricApi
import com.recco.core.openapi.model.AppUserMetricActionDTO
import com.recco.core.openapi.model.AppUserMetricCategoryDTO
import com.recco.core.openapi.model.AppUserMetricEventDTO
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