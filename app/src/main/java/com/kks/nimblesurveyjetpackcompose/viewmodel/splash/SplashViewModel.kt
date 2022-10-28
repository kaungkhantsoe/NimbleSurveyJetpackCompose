package com.kks.nimblesurveyjetpackcompose.viewmodel.splash

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.ErrorModel
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import com.kks.nimblesurveyjetpackcompose.util.PREF_REFRESH_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType
import com.kks.nimblesurveyjetpackcompose.util.extensions.mapError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(
    val shouldShowLoading: Boolean = false,
    val shouldNavigateToLogin: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val error: ErrorModel? = null
)

class SplashUiStatePreviewParameterProvider : PreviewParameterProvider<SplashUiState> {
    override val values: Sequence<SplashUiState> = sequenceOf(
        SplashUiState(shouldShowLoading = true),
        SplashUiState(shouldNavigateToLogin = true),
        SplashUiState(isLoginSuccess = true),
        SplashUiState(error = ErrorModel(ErrorType.INFO, errorMessage = "Error"))
    )
}

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
            if (preferenceManager.getStringData(PREF_REFRESH_TOKEN).isNullOrEmpty()) {
                _splashUiState.value = _splashUiState.value.copy(shouldNavigateToLogin = true)
            } else {
                _splashUiState.value = _splashUiState.value.copy(isLoginSuccess = true)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(ioDispatcher) {
            loginRepo.loginWithEmailAndPassword(email = email, password = password).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _splashUiState.value = _splashUiState.value.copy(shouldShowLoading = true)
                        resetError()
                    }
                    is ResourceState.Success -> {
                        _splashUiState.value =
                            _splashUiState.value.copy(isLoginSuccess = true, shouldShowLoading = false)
                        resetError()
                    }
                    else -> {
                        _splashUiState.value =
                            _splashUiState.value.copy(shouldShowLoading = false, error = it.mapError())
                    }
                }
            }
        }
    }

    fun resetError() {
        _splashUiState.value = _splashUiState.value.copy(error = null)
    }
}
