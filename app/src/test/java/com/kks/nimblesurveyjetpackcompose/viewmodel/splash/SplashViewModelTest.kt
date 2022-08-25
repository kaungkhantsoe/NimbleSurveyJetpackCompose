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

    override fun setup() {
        super.setup()
        viewModel = SplashViewModel(ioDispatcher = testDispatcher)
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is false at first`() {
        assertEquals(false, viewModel.shouldNavigateToLogin.value)
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is true after 2 seconds`() {
        val twoSeconds = 2000L
        runTest {
            viewModel.startTimerToNavigateToLogin()

            var actual = false
            launch {
                delay(twoSeconds)
                actual = viewModel.shouldNavigateToLogin.value
            }
            advanceUntilIdle()

            assertEquals(true, actual)
        }
    }
}
