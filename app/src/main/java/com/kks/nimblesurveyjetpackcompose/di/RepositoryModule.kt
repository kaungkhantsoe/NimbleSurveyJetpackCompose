package com.kks.nimblesurveyjetpackcompose.di

import com.kks.nimblesurveyjetpackcompose.network.Api
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepoImpl
import com.kks.nimblesurveyjetpackcompose.util.CustomKeyProvider
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideLoginRepo(
        api: Api,
        preferenceManager: PreferenceManager,
        customKeyProvider: CustomKeyProvider
    ): LoginRepo = LoginRepoImpl(api, preferenceManager, customKeyProvider)
}
