package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import com.kks.nimblesurveyjetpackcompose.surveys
import com.kks.nimblesurveyjetpackcompose.ui.theme.NimbleSurveyJetpackComposeTheme
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeViewModel
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class HomeScreenTest : BaseAndroidComposeTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var homeRepo: HomeRepo

    @BindValue
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun when_home_screen_starts_show_shimmer() {
        with(composeTestRule) {
            every { homeRepo.fetchSurveyList(any(), any(), any()) } returns flowOf(ResourceState.Loading)
            every { homeRepo.getSurveyListFromDb() } returns flowOf(emptyList())
            setupHomeComposeRule()
            onNodeWithContentDescription(getString(R.string.home_shimmer)).assertIsDisplayed()
        }
    }

    @Test
    fun when_get_survey_list_is_successful_show_first_survey() {
        with(composeTestRule) {
            every { homeRepo.fetchSurveyList(any(), any(), any()) } returns flowOf(ResourceState.Success(Unit))
            every { homeRepo.getSurveyListFromDb() } returns flowOf(surveys)
            setupHomeComposeRule()
            onNodeWithText(surveys.first().title).assertIsDisplayed()
            onNodeWithText(surveys.first().description).assertIsDisplayed()
            onAllNodesWithContentDescription(getString(R.string.home_dot)).assertCountEquals(2)
        }
    }

    @Test
    fun when_swipe_show_next_survey() {
        with(composeTestRule) {
            every { homeRepo.fetchSurveyList(any(), any(), any()) } returns flowOf(ResourceState.Success(Unit))
            every { homeRepo.getSurveyListFromDb() } returns flowOf(surveys)
            setupHomeComposeRule()
            onNodeWithContentDescription(getString(R.string.home_survey_content)).performTouchInput {
                swipeLeft()
            }
            waitForIdle()
            onNodeWithText(surveys[1].title).assertIsDisplayed()
            onNodeWithText(surveys[1].description).assertIsDisplayed()
        }
    }

    private fun setupHomeComposeRule() {
        every { homeRepo.fetchUserDetail() } returns flowOf(ResourceState.Loading)
        homeViewModel = HomeViewModel(homeRepo = homeRepo, ioDispatcher = Dispatchers.IO)
        composeTestRule.activity.setContent {
            NimbleSurveyJetpackComposeTheme {
                HomeScreen(
                    viewModel = homeViewModel,
                    navigator = EmptyDestinationsNavigator
                )
            }
        }
    }
}
