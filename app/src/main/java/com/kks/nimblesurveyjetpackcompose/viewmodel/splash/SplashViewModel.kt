package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _shouldNavigateToLogin = MutableStateFlow(false)

    val shouldNavigateToLogin: StateFlow<Boolean>
        get() = _shouldNavigateToLogin.asStateFlow()

    fun startTimerToNavigateToLogin(splashTime: Long = 2000L) {
        viewModelScope.launch(ioDispatcher) {
            delay(splashTime)
            _shouldNavigateToLogin.value = true
        }
    }
}
