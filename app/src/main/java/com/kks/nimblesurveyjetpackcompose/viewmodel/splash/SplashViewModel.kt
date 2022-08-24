package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.ErrorModel
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.util.PREF_LOGGED_IN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import com.kks.nimblesurveyjetpackcompose.util.extensions.mapError
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
    private val loginRepo: LoginRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val preferenceManager: PreferenceManager,
) : ViewModel() {
    private val _shouldShowLoading = MutableStateFlow(false)
    private val _shouldNavigateToLogin = MutableStateFlow(false)
    private val _isLoginSuccess = MutableStateFlow(false)
    private val _isAlreadyLoggedIn = MutableStateFlow(false)
    private var _error = MutableStateFlow<ErrorModel?>(null)

    val getError: StateFlow<ErrorModel?>
        get() = _error.asStateFlow()

    val shouldNavigateToLogin: StateFlow<Boolean>
        get() = _shouldNavigateToLogin.asStateFlow()

    val shouldShowLoading: StateFlow<Boolean>
        get() = _shouldShowLoading.asStateFlow()

    val isLoginSuccess: StateFlow<Boolean>
        get() = _isLoginSuccess.asStateFlow()

    val isAlreadyLoggedIn: StateFlow<Boolean>
        get() = _isAlreadyLoggedIn.asStateFlow()

    fun startTimerToNavigateToLogin(splashTime: Long) {
        viewModelScope.launch(ioDispatcher) {
            delay(splashTime)
            if (preferenceManager.getBooleanData(PREF_LOGGED_IN)) {
                _isAlreadyLoggedIn.value = true
            } else {
                _shouldNavigateToLogin.value = true
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(ioDispatcher) {
            loginRepo.loginWithEmailAndPassword(email = email, password = password).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _shouldShowLoading.value = true
                        resetError()
                    }
                    is ResourceState.Success -> {
                        _shouldShowLoading.value = false
                        _isLoginSuccess.value = true
                        resetError()
                    }
                    else -> {
                        _shouldShowLoading.value = false
                        it.mapError()?.let { errorModel ->
                            _error.value = errorModel
                        }
                    }
                }
            }
        }
    }

    fun resetError() {
        _error.value = null
    }
}
