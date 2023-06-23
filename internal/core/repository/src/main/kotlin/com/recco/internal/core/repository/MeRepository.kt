package com.recco.internal.core.repository

import com.recco.internal.core.model.recommendation.User
import com.recco.internal.core.network.http.unwrap
import com.recco.internal.core.openapi.api.AppUserApi
import com.recco.internal.core.repository.mapper.asEntity
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