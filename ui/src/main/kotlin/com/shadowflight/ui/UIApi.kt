package com.shadowflight.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import com.shadowflight.core.model.SDKConfig
import com.shadowflight.core.repository.AppRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
private interface UIApiInterface {
    fun getAppRepository(): AppRepository
}

object UIApi {
    private lateinit var application: Application

    fun init(sdkConfig: SDKConfig, application: Application) {
        this.application = application

        EntryPoints.get(application, UIApiInterface::class.java).getAppRepository()
            .init(sdkConfig)
    }

    fun login(userId: String) {
        EntryPoints
            .get(application, UIApiInterface::class.java).getAppRepository()
            .loginUser(userId)
    }

    fun logout() {
        EntryPoints
            .get(application, UIApiInterface::class.java).getAppRepository()
            .logoutUser()
    }

    fun navigateToDashboard(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}