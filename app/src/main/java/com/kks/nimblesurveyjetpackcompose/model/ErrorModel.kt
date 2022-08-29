package com.kks.nimblesurveyjetpackcompose.model

import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType

data class ErrorModel(
    val errorType: ErrorType,
    val errorMessage: String? = null
)
