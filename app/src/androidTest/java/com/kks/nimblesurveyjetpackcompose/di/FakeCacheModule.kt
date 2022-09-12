package com.kks.nimblesurveyjetpackcompose.di

import android.content.Context
import androidx.room.Room
import com.kks.nimblesurveyjetpackcompose.cache.SurveyDao
import com.kks.nimblesurveyjetpackcompose.cache.SurveyDatabase
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CacheModule::class]
)
class FakeCacheModule {

    @Singleton
    @Provides
    fun provideSurveyDatabase(
        @ApplicationContext app: Context
    ): SurveyDatabase = Room.inMemoryDatabaseBuilder(app, SurveyDatabase::class.java).build()

    @Singleton
    @Provides
    fun provideSurveyDao(db: SurveyDatabase): SurveyDao = db.surveyDao()

    @Singleton
    @Provides
    fun providePreferenceManager(): PreferenceManager = mockk(relaxed = true)
}
