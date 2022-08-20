package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import com.kks.nimblesurveyjetpackcompose.base.BaseViewModelTest
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SplashViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: SplashViewModel
    private val loginRepo: LoginRepo = mockk()

    override fun setup() {
        super.setup()
        viewModel = SplashViewModel(loginRepo = loginRepo, ioDispatcher = testDispatcher)
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is false at first`() {
        assertEquals(false, viewModel.shouldNavigateToLogin())
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is true after 2 seconds`() =
        runTest {
            viewModel.startTimerToNavigateToLogin()

            var actual = false
            launch {
                delay(SPLASH_TIME)
                actual = viewModel.shouldNavigateToLogin()
            }
            advanceUntilIdle()

            assertEquals(true, actual)
        }
}
