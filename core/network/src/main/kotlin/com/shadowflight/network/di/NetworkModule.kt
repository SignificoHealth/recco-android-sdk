package com.shadowflight.network.di

import com.shadowflight.logger.Logger
import com.shadowflight.model.SDKConfig
import com.shadowflight.model.UserAuthCredentials
import com.shadowflight.network.http.AddHeadersInterceptor
import com.shadowflight.network.http.ApiEndpoint
import com.shadowflight.network.http.AuthInterceptor
import com.shadowflight.network.moshi.OffsetDateTimeAdapter
import com.shadowflight.openapi.api.AppUserApi
import com.shadowflight.openapi.api.AuthenticationApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT = 30L

    @Provides
    fun provideApiEndpoint(sdkConfig: SDKConfig): ApiEndpoint = if (!sdkConfig.isDebug) {
        ApiEndpoint.PROD
    } else {
        ApiEndpoint.LOCAL
    }

    @Singleton
    @Provides
    fun provideAuthenticationApi(
        @RetrofitBase retrofit: Retrofit
    ): AuthenticationApi = retrofit.create(AuthenticationApi::class.java)

    @Singleton
    @Provides
    fun provideAppUserApi(
        @RetrofitAuthentication retrofit: Retrofit
    ): AppUserApi = retrofit.create(AppUserApi::class.java)

    @RetrofitBase
    @Provides
    fun provideRetrofit(
        moshi: Moshi,
        apiEndpoint: ApiEndpoint,
        sdkConfig: SDKConfig,
        logger: Logger
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiEndpoint.baseUrl)
        .client(
            buildOkhttp(
                isDebug = sdkConfig.isDebug,
                logger = logger
            )
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @RetrofitAuthentication
    @Provides
    fun provideRetrofitAuthentication(
        moshi: Moshi,
        apiEndpoint: ApiEndpoint,
        sdkConfig: SDKConfig,
        logger: Logger,
        userAuthCredentials: UserAuthCredentials,
        authenticationApi: AuthenticationApi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiEndpoint.baseUrl)
            .client(
                buildOkhttp(
                    isDebug = sdkConfig.isDebug,
                    logger = logger,
                    authInterceptor = AuthInterceptor(
                        sdkConfig,
                        userAuthCredentials,
                        authenticationApi
                    )
                )
            )
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun buildOkhttp(
        isDebug: Boolean,
        logger: Logger,
        authInterceptor: AuthInterceptor? = null
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (isDebug) {
                addInterceptor(
                    HttpLoggingInterceptor { message ->
                        logger.d(message, "OkHttp")
                    }.apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
            }

            addInterceptor(AddHeadersInterceptor())
            authInterceptor?.let { addInterceptor(authInterceptor) }

            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(OffsetDateTimeAdapter())
        .build()
}