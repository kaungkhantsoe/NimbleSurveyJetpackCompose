package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.SplashUiState
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.util.PREF_REFRESH_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import com.kks.nimblesurveyjetpackcompose.util.extensions.mapError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val preferenceManager: PreferenceManager,
) : ViewModel() {

    private val _splashUiState = MutableStateFlow(SplashUiState())

    val splashUiState: StateFlow<SplashUiState>
        get() = _splashUiState.asStateFlow()

    fun startTimerToNavigateToLogin(splashTime: Long) {
        viewModelScope.launch(ioDispatcher) {
            delay(splashTime)
            val shouldNavigateToLogin = preferenceManager.getStringData(PREF_REFRESH_TOKEN).isNullOrEmpty()
            _splashUiState.update {
                it.copy(
                    shouldNavigateToLogin = shouldNavigateToLogin,
                    isLoginSuccess = !shouldNavigateToLogin
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(ioDispatcher) {
            loginRepo.loginWithEmailAndPassword(email = email, password = password).collect { loginState ->
                when (loginState) {
                    is ResourceState.Loading -> {
                        _splashUiState.update { it.copy(shouldShowLoading = true) }
                        resetError()
                    }
                    is ResourceState.Success -> {
                        _splashUiState.update { it.copy(isLoginSuccess = true, shouldShowLoading = false) }
                        resetError()
                    }
                    else -> {
                        _splashUiState.update { it.copy(shouldShowLoading = false, error = loginState.mapError()) }
                    }
                }
            }
        }
    }

    fun resetError() {
        _splashUiState.update { it.copy(error = null) }
    }
}
