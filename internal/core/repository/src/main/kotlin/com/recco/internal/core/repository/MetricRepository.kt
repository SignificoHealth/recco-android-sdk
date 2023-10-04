package com.recco.internal.core.repository

import com.recco.internal.core.base.di.ApplicationScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.metric.AppUserMetricEvent
import com.recco.internal.core.openapi.api.MetricApi
import com.recco.internal.core.persistence.AuthCredentials
import com.recco.internal.core.repository.mapper.asDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetricRepository @Inject constructor(
    private val metricApi: MetricApi,
    private val authCredentials: AuthCredentials,
    private val logger: Logger,
    @ApplicationScope private val appScope: CoroutineScope
) {
    fun logEvent(event: AppUserMetricEvent) {
        authCredentials.userId?.let {
            appScope.launch {
                runCatching { metricApi.logEvent(event.asDTO()) }
                    .onFailure(logger::e)
            }
        }
    }
}
