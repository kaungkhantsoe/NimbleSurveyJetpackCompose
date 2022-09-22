package com.kks.nimblesurveyjetpackcompose.viewmodel.home

import com.kks.nimblesurveyjetpackcompose.base.BaseViewModelTest
import com.kks.nimblesurveyjetpackcompose.model.Meta
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseViewModelTest() {
    private lateinit var viewModel: HomeViewModel
    private val homeRepo: HomeRepo = mockk()
    private val survey: Survey = mockk()

    override fun setup() {
        super.setup()
        viewModel = HomeViewModel(homeRepo = homeRepo, ioDispatcher = testDispatcher)
    }

    @Test
    fun `When view model is created, survey list with pageNumber 1 is fetched`() = runTest {
        advanceUntilIdle()
        verify {
            homeRepo.fetchSurveyList(
                pageNumber = 1,
                pageSize = any(),
                isClearCache = false
            )
        }
    }

    @Test
    fun `When view model is created, user detail is fetched`() = runTest {
        advanceUntilIdle()
        verify { homeRepo.fetchUserDetail() }
    }

    @Test
    fun `When view model is created, survey list from database is fetched`() = runTest {
        advanceUntilIdle()
        verify { homeRepo.getSurveyListFromDb() }
    }

    @Test
    fun `When survey list is fetched from remote and return error, error dialog is shown`() = runTest {
        val errorMessage = "error"
        every {
            homeRepo.fetchSurveyList(
                pageNumber = any(),
                pageSize = any(),
                isClearCache = any()
            )
        } returns flowOf(ResourceState.Error(errorMessage))

        advanceUntilIdle()
        assertEquals(viewModel.error.value?.errorMessage, errorMessage)
    }

    @Test
    fun `When survey list is fetched from remote successfully, return surveyModel list`() = runTest {
        every {
            homeRepo.fetchSurveyList(
                pageSize = any(),
                pageNumber = any(),
                isClearCache = any()
            )
        } returns flowOf(ResourceState.Success(Meta()))

        advanceUntilIdle()
        assertEquals(viewModel.surveyList.value, emptyList<Survey>())
    }

    @Test
    fun `When next page of survey list is fetched, survey list is called with incremented pageNumber`() = runTest {
        every { homeRepo.fetchUserDetail() } returns flowOf(ResourceState.Loading)
        every { homeRepo.getSurveyListFromDb() } returns flowOf(List(5) { survey })
        every {
            homeRepo.fetchSurveyList(
                pageNumber = any(),
                pageSize = any(),
                isClearCache = any()
            )
        } returns flowOf(ResourceState.Success(Meta(pages = 2, records = 10)))

        advanceUntilIdle()

        viewModel.getNextPage()

        advanceUntilIdle()
        verify {
            homeRepo.fetchSurveyList(
                pageNumber = 2,
                pageSize = any(),
                isClearCache = false
            )
        }
    }
}
