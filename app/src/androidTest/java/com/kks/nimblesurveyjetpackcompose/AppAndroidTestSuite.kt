package com.kks.nimblesurveyjetpackcompose

import com.kks.nimblesurveyjetpackcompose.ui.presentation.home.HomeScreenTest
import com.kks.nimblesurveyjetpackcompose.ui.presentation.splash.SplashScreenTest
import com.kks.nimblesurveyjetpackcompose.ui.presentation.survey.SurveyHomeDetailScreenKtTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@kotlinx.coroutines.ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    HomeScreenTest::class,
    SplashScreenTest::class,
    SurveyHomeDetailScreenKtTest::class
)
class AppUnitTestSuite
