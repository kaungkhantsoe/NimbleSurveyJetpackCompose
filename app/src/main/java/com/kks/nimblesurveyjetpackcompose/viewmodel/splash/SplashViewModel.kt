package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_TIME = 2000L
class SplashViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    val shouldNavigateToLogin = mutableStateOf(false)

    fun startTimerToNavigateToLogin() {
        viewModelScope.launch(dispatcher) {
            delay(SPLASH_TIME)
            shouldNavigateToLogin.value = true
        }
    }
}
