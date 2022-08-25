package com.kks.nimblesurveyjetpackcompose.model

sealed class ResourceState<out T> {
    object Loading : ResourceState<Nothing>()
    data class Success<out T>(val data: T) : ResourceState<T>()
    class Error(val error: String?) : ResourceState<Nothing>()
    object NetworkError : ResourceState<Nothing>()
}
