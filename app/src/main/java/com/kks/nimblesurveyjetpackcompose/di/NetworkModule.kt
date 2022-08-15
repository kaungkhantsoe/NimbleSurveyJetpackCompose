package com.kks.nimblesurveyjetpackcompose.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.kks.nimblesurveyjetpackcompose.BuildConfig
import com.kks.nimblesurveyjetpackcompose.model.ApiInterface
import com.kks.nimblesurveyjetpackcompose.repo.token.TokenRepo
import com.kks.nimblesurveyjetpackcompose.repo.token.TokenRepoImpl
import com.kks.nimblesurveyjetpackcompose.util.CustomKeyGenerator
import com.kks.nimblesurveyjetpackcompose.util.CustomKeyProvider
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import com.kks.nimblesurveyjetpackcompose.util.interceptors.CustomAccessTokenInterceptor
import com.kks.nimblesurveyjetpackcompose.util.interceptors.TokenAuthenticator
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT_SECONDS = 15L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext appContext: Context): PreferenceManager =
        PreferenceManager(appContext)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        preferenceManager: PreferenceManager,
        tokenRepo: Lazy<TokenRepoImpl>,
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(CustomAccessTokenInterceptor(preferenceManager, tokenRepo))
            .authenticator(
                TokenAuthenticator(
                    preferenceManager,
                    tokenRepo
                )
            ) /* Refresh token interceptor */
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideChucker(@ApplicationContext appContext: Context) =
        ChuckerInterceptor.Builder(appContext)
            .maxContentLength(250000L)
            .redactHeaders(listOf("Auth-Token"))
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    @Singleton
    @Provides
    fun provideTokenRepoImpl(
        apiInterface: ApiInterface,
        preferenceManager: PreferenceManager,
        customKeyProvider: CustomKeyProvider
    ): TokenRepo =
        TokenRepoImpl(
            apiInterface,
            preferenceManager,
            customKeyProvider
        )

    @Singleton
    @Provides
    fun provideCustomKeyProvider(@ApplicationContext appContext: Context): CustomKeyProvider = CustomKeyGenerator(appContext)
}
