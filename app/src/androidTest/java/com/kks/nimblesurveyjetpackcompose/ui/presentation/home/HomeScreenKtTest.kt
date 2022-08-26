package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.di.RepositoryModule
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class HomeScreenKtTest : BaseAndroidComposeTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val homeViewModel: HomeViewModel = HomeViewModel(FakeHomeRepo(), Dispatchers.IO)

    @Before
    fun setup() {
        hiltRule.inject()
    }
}
