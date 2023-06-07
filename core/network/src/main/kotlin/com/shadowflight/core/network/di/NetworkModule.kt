package com.shadowflight.core.network.di

import com.shadowflight.core.logger.Logger
import com.shadowflight.core.network.BuildConfig
import com.shadowflight.core.network.http.ApiEndpoint
import com.shadowflight.core.network.http.interceptors.AddHeadersInterceptor
import com.shadowflight.core.network.http.interceptors.AuthInterceptor
import com.shadowflight.core.network.http.interceptors.ErrorInterceptor
import com.shadowflight.core.network.moshi.OffsetDateTimeAdapter
import com.shadowflight.core.openapi.api.AppUserApi
import com.shadowflight.core.openapi.api.AuthenticationApi
import com.shadowflight.core.openapi.api.FeedApi
import com.shadowflight.core.openapi.api.RecommendationApi
import com.shadowflight.core.persistence.AuthCredentials
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
    fun provideApiEndpoint(): ApiEndpoint =
        if (!BuildConfig.DEBUG) {
            ApiEndpoint.PROD
        } else {
            ApiEndpoint.STAGING
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
        logger: Logger
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiEndpoint.baseUrl)
        .client(
            buildOkhttp(
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
        logger: Logger,
        authInterceptor: AuthInterceptor? = null
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            authInterceptor?.let { addInterceptor(authInterceptor) }

            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor { message ->
                        logger.d(message, "OkHttp")
                    }.apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
            }

            addInterceptor(AddHeadersInterceptor())
            addInterceptor(ErrorInterceptor())

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