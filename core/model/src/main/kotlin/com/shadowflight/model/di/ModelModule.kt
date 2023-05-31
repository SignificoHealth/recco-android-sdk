package com.shadowflight.model.di

import com.shadowflight.model.SDKConfig
import com.shadowflight.model.authentication.UserAuthCredentials
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ModelModule {
    private lateinit var sdkConfig: SDKConfig
    private var userAuthCredentials = UserAuthCredentials()

    fun setUpExternalDependencies(sdkConfig: SDKConfig) {
        this.sdkConfig = sdkConfig
    }

    fun setUserId(userId: String) {
        userAuthCredentials.setId(userId)
    }

    @Provides
    fun provideSDKConfig(): SDKConfig = sdkConfig

    @Provides
    fun provideUserAuthCredentials(): UserAuthCredentials = userAuthCredentials
}