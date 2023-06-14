package com.shadowflight.core.repository

import com.shadowflight.core.model.recommendation.User
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.AppUserApi
import com.shadowflight.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class MeRepository @Inject constructor(
    private val api: AppUserApi
) {
    private val mePipeline = Pipeline { api.get().unwrap().asEntity() }
    val me: Flow<User> = mePipeline.state
}