package com.kks.nimblesurveyjetpackcompose.di

import android.content.Context
import com.kks.nimblesurveyjetpackcompose.model.ApiInterface
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepoImpl
import com.kks.nimblesurveyjetpackcompose.util.CustomKeyGenerator
import com.kks.nimblesurveyjetpackcompose.util.CustomKeyProvider
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideLoginRepoImpl(
        apiInterface: ApiInterface,
        preferenceManager: PreferenceManager,
        customKeyProvider: CustomKeyProvider
    ): LoginRepo = LoginRepoImpl(apiInterface, preferenceManager, customKeyProvider)
}
