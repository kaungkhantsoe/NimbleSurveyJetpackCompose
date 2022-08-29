package com.kks.nimblesurveyjetpackcompose.di

import android.content.Context
import androidx.room.Room
import com.kks.nimblesurveyjetpackcompose.cache.DB_NAME
import com.kks.nimblesurveyjetpackcompose.cache.SurveyDao
import com.kks.nimblesurveyjetpackcompose.cache.SurveyDatabase
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @Singleton
    @Provides
    fun provideSurveyDatabase(
        @ApplicationContext app: Context
    ): SurveyDatabase = Room.databaseBuilder(app, SurveyDatabase::class.java, DB_NAME).build()

    @Singleton
    @Provides
    fun provideSurveyDao(db: SurveyDatabase): SurveyDao = db.surveyDao()

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext appContext: Context): PreferenceManager =
        PreferenceManager(appContext)
}
