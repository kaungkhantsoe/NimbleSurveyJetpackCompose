package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest : BaseAndroidComposeTest() {

    val splashViewModel: SplashViewModel = mockk(relaxed = true)

    @Before
    fun setup() {
        coEvery { splashViewModel.startTimerToNavigateToLogin() } returns Unit
    }

    @Test
    fun when_splash_screen_start_show_both_background_image_and_logo_image() {
        setupSplashComposeRule {
            coEvery { splashViewModel.shouldNavigateToLogin() } returns false
        }
        with(composeTestRule) {
            onNodeWithContentDescription(getString(R.string.splash_background_content_description))
                .assertIsDisplayed()
            onNodeWithContentDescription(getString(R.string.splash_logo_content_description))
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_navigate_to_login_show_emil_password_and_login_button() {
        setupSplashComposeRule {
            coEvery { splashViewModel.shouldNavigateToLogin() } returns true
        }
        with(composeTestRule) {
            onNodeWithText(getString(R.string.login_email)).assertIsDisplayed()
            onNodeWithText(getString(R.string.login_password)).assertIsDisplayed()
            onNodeWithText(getString(R.string.login_log_in)).assertIsDisplayed()

            onNodeWithText(getString(R.string.login_email)).assertIsNotFocused()
            onNodeWithText(getString(R.string.login_password)).assertIsNotFocused()
            onNodeWithText(getString(R.string.login_log_in)).assertHasClickAction()
        }
    }

    private fun setupSplashComposeRule(actionBeforeSetup: (() -> Unit)? = null) {
        actionBeforeSetup?.invoke()
        composeTestRule.setContent {
            NimbleSurveyJetpackComposeTheme {
                SplashScreen(viewModel = splashViewModel)
            }
        }
    }
}
