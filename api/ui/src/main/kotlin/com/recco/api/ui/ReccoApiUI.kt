package com.recco.api.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import com.recco.api.model.ReccoConfig
import com.recco.api.model.ReccoLogger
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.repository.AppRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
private interface ReccoApiUIInterface {
    fun getAppRepository(): AppRepository
    fun getLogger(): Logger
}

object ReccoApiUI {
    private lateinit var application: Application

    fun init(
        sdkConfig: ReccoConfig,
        application: Application,
        logger: ReccoLogger? = null
    ) {
        this.application = application

        EntryPoints.get(application, ReccoApiUIInterface::class.java).getAppRepository()
            .init(sdkConfig)

        EntryPoints.get(application, ReccoApiUIInterface::class.java).getLogger()
            .setupClientLogger(logger)
    }

    fun login(userId: String) {
        EntryPoints
            .get(application, ReccoApiUIInterface::class.java).getAppRepository()
            .loginUser(userId)
    }

    fun logout() {
        EntryPoints
            .get(application, ReccoApiUIInterface::class.java).getAppRepository()
            .logoutUser()
    }

    fun navigateToDashboard(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}
