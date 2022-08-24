package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType
import com.kks.nimblesurveyjetpackcompose.util.extensions.mapError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _shouldNavigateToLogin = MutableStateFlow(false)
    private val _isLoginSuccess = MutableStateFlow(false)
    private var _isError = MutableStateFlow(ErrorType.NONE to "")

    val isError: StateFlow<Pair<ErrorType, String>>
        get() = _isError.asStateFlow()

    fun resetError() {
        _isError.value = ErrorType.NONE to ""
    }

    val shouldNavigateToLogin: StateFlow<Boolean>
        get() = _shouldNavigateToLogin.asStateFlow()

    val shouldShowLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    val isLoginSuccess: StateFlow<Boolean>
        get() = _isLoginSuccess.asStateFlow()

    fun startTimerToNavigateToLogin() {
        viewModelScope.launch(ioDispatcher) {
            delay(splashTime)
            _shouldNavigateToLogin.value = true
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(ioDispatcher) {
            loginRepo.loginWithEmailAndPassword(email = email, password = password).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _isLoading.value = true
                        resetError()
                    }
                    is ResourceState.Success -> {
                        _isLoading.value = false
                        _isLoginSuccess.value = true
                        resetError()
                    }
                    else -> {
                        _isLoading.value = false
                        mapError(it, _isError)
                    }
                }
            }
        }
    }
}
