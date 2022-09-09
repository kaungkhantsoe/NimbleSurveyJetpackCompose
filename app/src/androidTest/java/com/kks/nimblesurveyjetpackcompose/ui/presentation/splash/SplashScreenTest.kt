package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

const val EMAIL = "example@gmail.com"
const val VALID_PASSWORD = "valid"
const val INVALID_PASSWORD = "invalid"
const val ERROR_MESSAGE = "error"
private const val SPLASH_TIME = 0L

@HiltAndroidTest
class SplashScreenTest : BaseAndroidComposeTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var loginRepo: LoginRepo

    @BindValue
    lateinit var splashViewModel: SplashViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        setupSplashComposeRule()
    }

    @Test
    fun when_splash_screen_start_show_both_background_image_and_logo_image() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.splash_background_content_description))
                .assertIsDisplayed()
            onNodeWithContentDescription(getString(R.string.splash_logo_content_description))
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_navigate_to_login_show_email_password_and_login_button() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithText(getString(R.string.login_email)).assertIsDisplayed()
            onNodeWithText(getString(R.string.login_password)).assertIsDisplayed()
            onNodeWithText(getString(R.string.login_log_in)).assertIsDisplayed()

            onNodeWithText(getString(R.string.login_email)).assertIsNotFocused()
            onNodeWithText(getString(R.string.login_password)).assertIsNotFocused()
            onNodeWithText(getString(R.string.login_log_in)).assertHasClickAction()
        }
    }

    @Test
    fun when_type_email_into_email_text_field_has_email_text() {
        with(composeTestRule) {
            waitForIdle()
            val email = "example@gmail.com"
            onNodeWithContentDescription(getString(R.string.login_email_text_field))
                .performTextInput(email)
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.login_email_text_field))
                .assert(hasText(email))
        }
    }

    @Test
    fun when_type_password_into_password_text_field_has_text_with_mask() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.login_password_text_field))
                .performTextInput("p")
            onNodeWithContentDescription(getString(R.string.login_password_text_field))
                .assert(hasText("\u2022"))
        }
    }

    @Test
    fun when_fill_in_only_one_field_login_button_is_disabled() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.login_email_text_field))
                .performTextInput("example@gmail.com")
            onNodeWithContentDescription(getString(R.string.login_log_in_button))
                .assertIsNotEnabled()
        }
    }

    @Test
    fun when_both_email_and_login_fields_login_button_is_enabled() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.login_email_text_field))
                .performTextInput("example@gmail.com")
            onNodeWithContentDescription(getString(R.string.login_password_text_field))
                .performTextInput("p")
            onNodeWithContentDescription(getString(R.string.login_log_in_button))
                .assertIsEnabled()
            onNodeWithContentDescription(getString(R.string.login_log_in_button))
                .assertHasClickAction()
        }
    }

    @Test
    fun when_with_correct_email_and_password_goes_to_home() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.login_email_text_field))
                .performTextInput(EMAIL)
            onNodeWithContentDescription(getString(R.string.login_password_text_field))
                .performTextInput(VALID_PASSWORD)
            onNodeWithContentDescription(getString(R.string.login_log_in_button))
                .performClick()
            waitForIdle()
            assertTrue(splashViewModel.isLoginSuccess.value)
        }
    }

    @Test
    fun when_with_incorrect_email_and_password_error_dialog_is_shown() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.login_email_text_field))
                .performTextInput(EMAIL)
            onNodeWithContentDescription(getString(R.string.login_password_text_field))
                .performTextInput(INVALID_PASSWORD)
            onNodeWithContentDescription(getString(R.string.login_log_in_button))
                .performClick()
            onNodeWithText(ERROR_MESSAGE).assertIsDisplayed()
        }
    }

    private fun setupSplashComposeRule() {
        every { preferenceManager.getStringData(any()) } returns ""
        splashViewModel = SplashViewModel(
            loginRepo = loginRepo,
            ioDispatcher = Dispatchers.IO,
            preferenceManager = preferenceManager
        )
        composeTestRule.activity.setContent {
            NimbleSurveyJetpackComposeTheme {
                SplashScreen(
                    splashTime = SPLASH_TIME,
                    viewModel = splashViewModel,
                    navigator = EmptyDestinationsNavigator
                )
            }
        }
    }
}
