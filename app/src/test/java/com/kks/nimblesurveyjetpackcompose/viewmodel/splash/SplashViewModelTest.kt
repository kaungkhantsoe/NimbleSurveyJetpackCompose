package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import com.kks.nimblesurveyjetpackcompose.base.BaseViewModelTest
import junit.framework.Assert.assertEquals
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
        viewModel = SplashViewModel(dispatcher = testDispatcher)
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is false at first`() {
        val actual = viewModel.shouldNavigateToLogin.value
        assertEquals(false, actual)
    }

    @Test
    fun `When splash screen is displayed, shouldNavigateToLogin value is true after 2 seconds`() =
        runTest {
            viewModel.startTimerToNavigateToLogin()

            var actual = false
            launch {
                delay(2000)
                actual = viewModel.shouldNavigateToLogin.value
            }
            advanceUntilIdle()

            assertEquals(true, actual)
        }
}
