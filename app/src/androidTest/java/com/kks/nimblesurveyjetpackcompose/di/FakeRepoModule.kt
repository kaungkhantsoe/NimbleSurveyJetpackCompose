package com.kks.nimblesurveyjetpackcompose.di

import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.repo.survey.SurveyRepo
import com.kks.nimblesurveyjetpackcompose.ui.presentation.splash.FakeLoginRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
class FakeRepoModule {
    @Singleton
    @Provides
    fun provideFakeLoginRepo(): LoginRepo = FakeLoginRepo()

    @Singleton
    @Provides
    fun provideFakeHomeRepo(): HomeRepo =  mockk()

    @Singleton
    @Provides
    fun provideFakeSurveyRepo(): SurveyRepo = mockk()
}
