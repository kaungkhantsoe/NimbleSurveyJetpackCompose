package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.di.RepositoryModule
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SPLASH_TIME
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val EMAIL = "example@gmail.com"
const val VALID_PASSWORD = "valid"
const val INVALID_PASSWORD = "invalid"
const val ERROR_MESSAGE = "error"

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class SplashScreenTest : BaseAndroidComposeTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val splashViewModel: SplashViewModel = SplashViewModel(FakeLoginRepo(), Dispatchers.IO)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun when_splash_screen_start_show_both_background_image_and_logo_image() {
        setupSplashComposeRule(2000)
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
        SPLASH_TIME = splashTime
        composeTestRule.setContent {
            NimbleSurveyJetpackComposeTheme {
                SplashScreen(viewModel = splashViewModel)
            }
        }
    }

    class FakeLoginRepo : LoginRepo {
        override fun loginWithEmailAndPassword(
            email: String,
            password: String
        ): Flow<ResourceState<LoginResponse>> {
            return if (password == VALID_PASSWORD) {
                flowOf(
                    ResourceState.Success(
                        LoginResponse(
                            id = null,
                            type = null,
                            attributes = null
                        )
                    )
                )
            } else {
                flowOf(ResourceState.Error(ERROR_MESSAGE))
            }
        }
    }
}
