package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.surveys
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.kks.nimblesurveyjetpackcompose.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class SurveyHomeDetailScreenKtTest : BaseAndroidComposeTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        setupSurveyHomeDetailScreen(surveys.first())
    }

    @Test
    fun when_navigate_to_survey_home_detail_screen_show_survey_detail() {
        with(composeTestRule) {
            onNodeWithText(surveys.first().title).assertIsDisplayed()
            onNodeWithText(surveys.first().description).assertIsDisplayed()
            onNodeWithContentDescription(getString(R.string.survey_detail_back_icon)).assertIsDisplayed()
            onNodeWithContentDescription(getString(R.string.survey_detail_start_survey)).assertIsDisplayed()
        }
    }

    @Test
    fun when_click_on_start_survey_button_the_button_disappeared() {
        with(composeTestRule) {
            onNodeWithContentDescription(getString(R.string.survey_detail_start_survey)).performClick()
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.survey_detail_start_survey)).assertIsNotDisplayed()
        }
    }

    private fun setupSurveyHomeDetailScreen(survey: Survey) {
        composeTestRule.activity.setContent {
            NimbleSurveyJetpackComposeTheme {
                SurveyHomeDetailScreen(navigator = EmptyDestinationsNavigator, survey = survey)
            }
        }
    }
}
