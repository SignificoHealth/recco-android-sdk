package com.recco.core.repository

import com.recco.core.model.recommendation.User
import com.recco.core.network.http.unwrap
import com.recco.core.openapi.api.AppUserApi
import com.recco.core.repository.mapper.asEntity
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