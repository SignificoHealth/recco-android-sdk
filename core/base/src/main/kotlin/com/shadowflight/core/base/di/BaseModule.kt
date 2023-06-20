package com.shadowflight.core.base.di

import com.vilua.core.di.ApplicationScope
import com.vilua.core.di.DefaultDispatcher
import com.vilua.core.di.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher() = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(@DefaultDispatcher dispatcher: CoroutineDispatcher) =
        CoroutineScope(context = SupervisorJob() + dispatcher)
}
