package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenSplashScreenStart_showBothBackgroundImageAndLogoImage() {
        composeTestRule.setContent {
            NimbleSurveyJetpackComposeTheme {
                SplashScreen(EmptyDestinationsNavigator)
            }
        }
        composeTestRule.onNodeWithContentDescription(SPLASH_BACKGROUND_IMAGE).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(SPLASH_LOGO_IMAGE).assertIsDisplayed()
    }
}
