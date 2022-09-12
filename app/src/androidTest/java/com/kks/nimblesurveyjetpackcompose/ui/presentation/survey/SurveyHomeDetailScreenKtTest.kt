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
import org.junit.Before
import org.junit.Test
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.repo.survey.SurveyRepo
import com.kks.nimblesurveyjetpackcompose.surveyQuestions
import com.kks.nimblesurveyjetpackcompose.viewmodel.survey.SurveyDetailViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
class SurveyHomeDetailScreenKtTest : BaseAndroidComposeTest() {

    lateinit var surveyDetailViewModel: SurveyDetailViewModel

    @Inject
    lateinit var surveyRepo: SurveyRepo

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        every { surveyRepo.getSurveyDetails(any()) } returns flowOf(ResourceState.Success(surveyQuestions))
        surveyDetailViewModel = SurveyDetailViewModel(surveyRepo = surveyRepo, ioDispatcher = Dispatchers.IO)
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

    @Test
    fun when_click_on_next_question_button_the_button_disappeared_and_next_question_slide_in() {
        with(composeTestRule) {
            onNodeWithContentDescription(getString(R.string.survey_detail_start_survey)).performClick()
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.survey_detail_start_survey)).assertIsNotDisplayed()
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.survey_question_next_question)).assertIsDisplayed()
            onNodeWithText(surveyQuestions.first().title).assertIsDisplayed()
        }
    }

    @Test
    fun when_click_on_next_question_button_and_reach_the_end_of_survey_list_show_submit_button() {
        with(composeTestRule) {
            onNodeWithContentDescription(getString(R.string.survey_detail_start_survey)).performClick()
            waitForIdle()
            onNodeWithContentDescription(getString(R.string.survey_question_next_question)).performClick()
            waitForIdle()
            onNodeWithText(getString(R.string.survey_question_submit_survey)).assertIsDisplayed()
        }
    }

    private fun setupSurveyHomeDetailScreen(survey: Survey) {
        composeTestRule.activity.setContent {
            NimbleSurveyJetpackComposeTheme {
                SurveyDetailScreen(
                    navigator = EmptyDestinationsNavigator,
                    survey = survey,
                    viewModel = surveyDetailViewModel
                )
            }
        }
    }
}
