package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SPLASH_TIME = 2000L

@Suppress("UnusedPrivateMember")
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _shouldNavigateToLogin = mutableStateOf(false)

    fun shouldNavigateToLogin() = _shouldNavigateToLogin.value

    fun startTimerToNavigateToLogin() {
        viewModelScope.launch(ioDispatcher) {
            delay(SPLASH_TIME)
            _shouldNavigateToLogin.value = true
        }
    }
}
