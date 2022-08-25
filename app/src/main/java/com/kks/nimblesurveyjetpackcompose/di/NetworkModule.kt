package com.kks.nimblesurveyjetpackcompose.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.kks.nimblesurveyjetpackcompose.BuildConfig
import com.kks.nimblesurveyjetpackcompose.network.ApiInterface
import com.kks.nimblesurveyjetpackcompose.network.AuthInterface
import com.kks.nimblesurveyjetpackcompose.repo.token.TokenRepo
import com.kks.nimblesurveyjetpackcompose.repo.token.TokenRepoImpl
import com.kks.nimblesurveyjetpackcompose.util.CustomKeyProvider
import com.kks.nimblesurveyjetpackcompose.util.CustomKeyProviderImpl
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import com.kks.nimblesurveyjetpackcompose.util.interceptors.AccessTokenInterceptor
import com.kks.nimblesurveyjetpackcompose.util.interceptors.TokenAuthenticator
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT_SECONDS = 15L
private const val CHUCKER_MAX_CONTENT_LENGTH = 250000L

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun provideTokenAuthenticator(tokenAuthenticator: TokenAuthenticator): Authenticator

    @Binds
    fun provideAuthAccessTokenInterceptor(accessTokenInterceptor: AccessTokenInterceptor): Interceptor

    @Suppress("TooManyFunctions")
    companion object {
        @Singleton
        @Provides
        fun providePreferenceManager(@ApplicationContext appContext: Context): PreferenceManager =
            PreferenceManager(appContext)

        @ServiceQualifier
        @Provides
        fun provideServiceRetrofit(
            @ServiceQualifier okHttpClient: OkHttpClient,
            moshi: Moshi
        ): Retrofit {
            return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        @AuthQualifier
        @Provides
        fun provideAuthRetrofit(
            @AuthQualifier okHttpClient: OkHttpClient,
            moshi: Moshi
        ): Retrofit {
            return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        @Singleton
        @Provides
        fun providesMoshi(): Moshi {
            return Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
        }

        @ServiceQualifier
        @Provides
        fun provideServiceOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            chuckerInterceptor: ChuckerInterceptor,
            tokenAuthenticator: TokenAuthenticator,
            accessTokenInterceptor: Interceptor
        ): OkHttpClient {
            return OkHttpClient().newBuilder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(chuckerInterceptor)
                .addInterceptor(accessTokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .authenticator(tokenAuthenticator)
                .build()
        }

        @AuthQualifier
        @Provides
        fun provideAuthOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            chuckerInterceptor: ChuckerInterceptor,
            accessTokenInterceptor: Interceptor
        ): OkHttpClient {
            return OkHttpClient().newBuilder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(chuckerInterceptor)
                .addInterceptor(accessTokenInterceptor) /* Refresh token interceptor */
                .addInterceptor(loggingInterceptor)
                .build()
        }

        @Singleton
        @Provides
        fun provideChucker(@ApplicationContext appContext: Context): ChuckerInterceptor =
            ChuckerInterceptor.Builder(appContext)
                .maxContentLength(CHUCKER_MAX_CONTENT_LENGTH)
                .redactHeaders(listOf("Auth-Token"))
                .build()

        @Provides
        fun provideApiService(@ServiceQualifier retrofit: Retrofit): ApiInterface =
            retrofit.create(ApiInterface::class.java)

        @Singleton
        @Provides
        fun provideAuthService(@AuthQualifier retrofit: Retrofit): AuthInterface =
            retrofit.create(AuthInterface::class.java)

        @Singleton
        @Provides
        fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            )

        @Singleton
        @Provides
        fun provideTokenRepo(
            authInterface: AuthInterface,
            preferenceManager: PreferenceManager,
            customKeyProvider: CustomKeyProvider
        ): TokenRepo = TokenRepoImpl(
            authInterface,
            preferenceManager,
            customKeyProvider
        )

        @Singleton
        @Provides
        fun provideCustomKeyProvider(@ApplicationContext appContext: Context): CustomKeyProvider =
            CustomKeyProviderImpl(appContext)
    }
}
