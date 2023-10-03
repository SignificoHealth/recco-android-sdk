package com.recco.api.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ProcessLifecycleOwner
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
    fun getApplicationLifecycleObserver(): ApplicationLifecycleObserver
}

object ReccoApiUI {
    private lateinit var application: Application

    /**
     * init
     *
     * Configures Recco given an application name and and clientSecret.
     *
     * UI can be customized setting a style from the currently available [Fresh, Ocean, Spring, Tech]
     *
     * Debug and error log operations can be enabled providing your own ReccoLogger implementation.
     *
     * @param sdkConfig [ReccoConfig] Initial configuration for the SDK to work properly.
     * @param application Android context.
     * @param logger [ReccoLogger] Logger contract implementation.
     */
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

        application.mainLooper.run {
            EntryPoints.get(application, ReccoApiUIInterface::class.java).getApplicationLifecycleObserver()
                .register(ProcessLifecycleOwner.get().lifecycle)
        }
    }

    /**
     * login
     *
     * Sets user identifiers, and triggers login operation. User session login is tracked.
     *
     * @param userId identifier that enables Recco experience to be unique and custom.
     */
    fun login(userId: String) {
        EntryPoints
            .get(application, ReccoApiUIInterface::class.java).getAppRepository()
            .loginUser(userId)
    }

    /**
     * logout
     *
     * Clears user credentials, cached data and triggers logout operation.
     */
    fun logout() {
        EntryPoints
            .get(application, ReccoApiUIInterface::class.java).getAppRepository()
            .logoutUser()
    }

    /**
     * navigateToDashboard
     *
     * Starts Recco experience by launching MainActivity on top of the current navigation stack.
     *
     * @param context Android context.
     */
    fun navigateToDashboard(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}
