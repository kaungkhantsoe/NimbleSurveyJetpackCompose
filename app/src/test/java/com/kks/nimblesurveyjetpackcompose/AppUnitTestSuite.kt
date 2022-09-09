package com.kks.nimblesurveyjetpackcompose

import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeViewModelTest
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@kotlinx.coroutines.ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    HomeViewModelTest::class,
    SplashViewModelTest::class
)
class AppUnitTestSuite
