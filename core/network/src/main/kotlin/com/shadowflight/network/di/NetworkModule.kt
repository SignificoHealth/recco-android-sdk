package com.shadowflight.network.di

import com.shadowflight.logger.Logger
import com.shadowflight.model.SDKConfig
import com.shadowflight.network.http.AddHeadersInterceptor
import com.shadowflight.network.http.ApiEndpoint
import com.shadowflight.network.http.AuthInterceptor
import com.shadowflight.network.moshi.OffsetDateTimeAdapter
import com.shadowflight.openapi.api.AppUserApi
import com.shadowflight.openapi.api.AuthenticationApi
import com.shadowflight.openapi.api.FeedApi
import com.shadowflight.openapi.api.RecommendationApi
import com.shadowflight.persistence.AuthCredentials
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

    @Singleton
    @Provides
    fun provideApiEndpoint(authCredentials: AuthCredentials): ApiEndpoint =
        if (!authCredentials.sdkConfig.isDebug) {
            ApiEndpoint.PROD
        } else {
            ApiEndpoint.LOCAL
        }

    @Singleton
    @Provides
    fun provideAuthenticationApi(@RetrofitBase retrofit: Retrofit): AuthenticationApi =
        retrofit.create(AuthenticationApi::class.java)

    @Singleton
    @Provides
    fun provideAppUserApi(@RetrofitAuthentication retrofit: Retrofit): AppUserApi =
        retrofit.create(AppUserApi::class.java)

    @Singleton
    @Provides
    fun provideFeedApi(@RetrofitAuthentication retrofit: Retrofit): FeedApi =
        retrofit.create(FeedApi::class.java)

    @Singleton
    @Provides
    fun provideRecommendationApi(@RetrofitAuthentication retrofit: Retrofit): RecommendationApi =
        retrofit.create(RecommendationApi::class.java)

    @RetrofitBase
    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: Moshi,
        apiEndpoint: ApiEndpoint,
        authCredentials: AuthCredentials,
        logger: Logger
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiEndpoint.baseUrl)
        .client(
            buildOkhttp(
                isDebug = authCredentials.sdkConfig.isDebug,
                logger = logger
            )
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @RetrofitAuthentication
    @Singleton
    @Provides
    fun provideRetrofitAuthentication(
        moshi: Moshi,
        apiEndpoint: ApiEndpoint,
        logger: Logger,
        authCredentials: AuthCredentials,
        authenticationApi: AuthenticationApi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiEndpoint.baseUrl)
            .client(
                buildOkhttp(
                    isDebug = authCredentials.sdkConfig.isDebug,
                    logger = logger,
                    authInterceptor = AuthInterceptor(
                        authCredentials,
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
            authInterceptor?.let { addInterceptor(authInterceptor) }

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

            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(OffsetDateTimeAdapter())
        .build()
}