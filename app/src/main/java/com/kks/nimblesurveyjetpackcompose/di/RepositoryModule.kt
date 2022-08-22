package com.kks.nimblesurveyjetpackcompose.di

import com.kks.nimblesurveyjetpackcompose.model.ApiInterface
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepoImpl
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
        apiInterface: ApiInterface,
        preferenceManager: PreferenceManager,
        customKeyProvider: CustomKeyProvider
    ): LoginRepo = LoginRepoImpl(apiInterface, preferenceManager, customKeyProvider)

    @Provides
    fun provideHomeRepoImpl(
        apiInterface: ApiInterface
    ): HomeRepo = HomeRepoImpl(apiInterface)
}
