package com.kks.nimblesurveyjetpackcompose.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kks.nimblesurveyjetpackcompose.model.ResourceState

abstract class BaseViewModel : ViewModel() {
    private var _isError = mutableStateOf(ErrorType.NONE to "")

    fun isError() = _isError.value

    fun resetError() { _isError.value = ErrorType.NONE to "" }

    protected fun <T> mapError(resourceState: ResourceState<T>) {
        when (resourceState) {
            is ResourceState.Error -> _isError.value = ErrorType.INFO to resourceState.error
            is ResourceState.GenericError ->
                _isError.value = ErrorType.GENERIC to resourceState.error.orEmpty()
            is ResourceState.NetworkError -> _isError.value = ErrorType.NETWORK to ""
            is ResourceState.ProtocolError -> _isError.value = ErrorType.PROTOCOL to ""
            else -> {
                // Do nothing
            }
        }
    }
}

enum class ErrorType {
    NONE, INFO, GENERIC, NETWORK, PROTOCOL
}
