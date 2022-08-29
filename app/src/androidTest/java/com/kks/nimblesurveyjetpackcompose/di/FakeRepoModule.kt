package com.kks.nimblesurveyjetpackcompose.di

import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.ui.presentation.splash.FakeLoginRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
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
}
