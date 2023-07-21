package com.recco.internal.core.network.di

import com.recco.internal.core.base.di.IoDispatcher
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.network.BuildConfig
import com.recco.internal.core.network.http.interceptors.AddHeadersInterceptor
import com.recco.internal.core.network.http.interceptors.AuthInterceptor
import com.recco.internal.core.network.http.interceptors.ErrorInterceptor
import com.recco.internal.core.network.moshi.OffsetDateTimeAdapter
import com.recco.internal.core.openapi.api.AppUserApi
import com.recco.internal.core.openapi.api.AuthenticationApi
import com.recco.internal.core.openapi.api.FeedApi
import com.recco.internal.core.openapi.api.MetricApi
import com.recco.internal.core.openapi.api.QuestionnaireApi
import com.recco.internal.core.openapi.api.RecommendationApi
import com.recco.internal.core.persistence.AuthCredentials
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    var BASE_URL = "https://api.sf-dev.significo.dev/"

    private const val TIMEOUT = 30L

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

    @Singleton
    @Provides
    fun provideQuestionnaireApi(@RetrofitAuthentication retrofit: Retrofit): QuestionnaireApi =
        retrofit.create(QuestionnaireApi::class.java)

    @Singleton
    @Provides
    fun provideMetricApi(@RetrofitAuthentication retrofit: Retrofit): MetricApi =
        retrofit.create(MetricApi::class.java)

    @RetrofitBase
    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: Moshi,
        logger: Logger
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
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
        logger: Logger,
        authCredentials: AuthCredentials,
        authenticationApi: AuthenticationApi,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                buildOkhttp(
                    logger = logger,
                    authInterceptor = AuthInterceptor(
                        authCredentials = authCredentials,
                        authenticationApi = authenticationApi,
                        dispatcher = dispatcher
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
