package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.di.RepositoryModule
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class SplashScreenTest : BaseAndroidComposeTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val splashViewModel: SplashViewModel = SplashViewModel(Dispatchers.IO)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun when_splash_screen_start_show_both_background_image_and_logo_image() {
        setupSplashComposeRule()
        with(composeTestRule) {
            onNodeWithContentDescription(getString(R.string.splash_background_content_description))
                .assertIsDisplayed()
            onNodeWithContentDescription(getString(R.string.splash_logo_content_description))
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_navigate_to_login_show_emil_password_and_login_button() {
        setupSplashComposeRule()
        with(composeTestRule) {
            onNodeWithText(getString(R.string.login_email)).assertIsDisplayed()
            onNodeWithText(getString(R.string.login_password)).assertIsDisplayed()
            onNodeWithText(getString(R.string.login_log_in)).assertIsDisplayed()

            onNodeWithText(getString(R.string.login_email)).assertIsNotFocused()
            onNodeWithText(getString(R.string.login_password)).assertIsNotFocused()
            onNodeWithText(getString(R.string.login_log_in)).assertHasClickAction()
        }
    }

    private fun setupSplashComposeRule(splashTime: Long = 0L) {
        composeTestRule.activity.setContent {
            NimbleSurveyJetpackComposeTheme {
                SplashScreen(splashTime = splashTime)
            }
        }
    }
}
