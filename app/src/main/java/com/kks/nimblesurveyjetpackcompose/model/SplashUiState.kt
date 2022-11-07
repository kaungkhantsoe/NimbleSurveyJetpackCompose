package com.kks.nimblesurveyjetpackcompose.model

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType

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
