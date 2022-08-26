package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import com.kks.nimblesurveyjetpackcompose.base.BaseViewModelTest
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.util.PREF_LOGGED_IN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SplashViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: SplashViewModel
    private val loginRepo: LoginRepo = mockk()
    private val preferenceManager: PreferenceManager = mockk()

    override fun setup() {
        super.setup()
        viewModel = SplashViewModel(
            loginRepo = loginRepo,
            ioDispatcher = testDispatcher,
            preferenceManager = preferenceManager
        )
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is false at first`() {
        assertEquals(false, viewModel.shouldNavigateToLogin.value)
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is true after 2 seconds`() {
        every { preferenceManager.getBooleanData(PREF_LOGGED_IN) } returns false
        val twoSeconds = 2000L
        runTest {
            viewModel.startTimerToNavigateToLogin(twoSeconds)

            var actual = false
            launch {
                delay(twoSeconds)
                actual = viewModel.shouldNavigateToLogin.value
            }
            advanceUntilIdle()

            assertEquals(true, actual)
        }
    }

    @Test
    fun `When login with incorrect email or password, show error`() = runTest {
        val errorMessage = "error"
        val errorState: ResourceState<LoginResponse> = ResourceState.Error(errorMessage)
        coEvery { loginRepo.loginWithEmailAndPassword(any(), any()) } returns flowOf(errorState)

        viewModel.login("example@gmail.com", "invalid")
        advanceUntilIdle()

        assertEquals(errorMessage, viewModel.error.value?.errorMessage)
    }

    @Test
    fun `When login with correct email or password, login success`() = runTest {
        val loginResponse = LoginResponse(id = 0, type = null, attributes = null)
        val errorState: ResourceState<LoginResponse> = ResourceState.Success(loginResponse)
        coEvery { loginRepo.loginWithEmailAndPassword(any(), any()) } returns flowOf(errorState)

        viewModel.login("example@gmail.com", "valid")
        advanceUntilIdle()

        assertEquals(true, viewModel.isLoginSuccess.value)
    }
}
