package com.shadowflight.repository

import com.shadowflight.network.http.unwrap
import com.shadowflight.openapi.api.AppUserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeRepository @Inject constructor(
    private val api: AppUserApi
) {
    fun getMyId(): Flow<String> = flow {
        emit(api.get().unwrap().id)
    }
}