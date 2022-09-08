package com.kks.nimblesurveyjetpackcompose.viewmodel.survey

import com.kks.nimblesurveyjetpackcompose.base.BaseViewModelTest
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.repo.survey.SurveyRepo
import com.kks.nimblesurveyjetpackcompose.surveyQuestions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class SurveyDetailViewModelTest : BaseViewModelTest() {
    private lateinit var viewModel: SurveyDetailViewModel
    private val surveyRepo: SurveyRepo = mockk()

    override fun setup() {
        super.setup()
        viewModel = SurveyDetailViewModel(surveyRepo = surveyRepo, ioDispatcher = testDispatcher)
    }

    @Test
    fun `When get survey questions and return error, show error`() = runTest {
        val errorMessage = "error"
        val errorState: ResourceState<List<SurveyQuestion>> = ResourceState.Error(errorMessage)
        coEvery { surveyRepo.getSurveyDetails(any()) } returns flowOf(errorState)

        viewModel.getSurveyQuestions(surveyId = "1")
        advanceUntilIdle()

        assertEquals(errorMessage, viewModel.error.value?.errorMessage)
    }

    @Test
    fun `When get survey questions and return success, show survey list`() = runTest {
        val successState: ResourceState<List<SurveyQuestion>> = ResourceState.Success(surveyQuestions)
        coEvery { surveyRepo.getSurveyDetails(any()) } returns flowOf(successState)

        viewModel.getSurveyQuestions(surveyId = "1")
        advanceUntilIdle()

        assertEquals(surveyQuestions, viewModel.surveyQuestions.value)
    }

    @Test
    fun `When submit survey and return error, show error`() = runTest {
        val errorMessage = "error"
        val errorState: ResourceState<Unit> = ResourceState.Error(errorMessage)
        coEvery { surveyRepo.submitSurvey(any(), any()) } returns flowOf(errorState)

        viewModel.submitSurvey(surveyId = "1")
        advanceUntilIdle()

        assertEquals(errorMessage, viewModel.error.value?.errorMessage)
    }

    @Test
    fun `When submit survey and return success, show lottie`() = runTest {
        val successState: ResourceState<Unit> = ResourceState.Success(Unit)
        coEvery { surveyRepo.submitSurvey(any(), any()) } returns flowOf(successState)

        viewModel.submitSurvey(surveyId = "1")
        advanceUntilIdle()

        assertEquals(true, viewModel.shouldShowLottie.value)
    }
}
