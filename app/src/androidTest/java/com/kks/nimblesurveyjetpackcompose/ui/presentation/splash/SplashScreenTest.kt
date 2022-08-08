package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.ui.presentation.main.MainActivity
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun when_splash_screen_start_show_both_background_image_and_logo_image() {
        composeTestRule.setContent {
            NimbleSurveyJetpackComposeTheme {
                SplashScreen(EmptyDestinationsNavigator)
            }
        }
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.splash_background_content_description))
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.splash_logo_content_description))
            .assertIsDisplayed()
    }
}
