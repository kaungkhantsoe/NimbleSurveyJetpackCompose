package com.kks.nimblesurveyjetpackcompose.model

import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType

data class ErrorModel(
    val errorType: ErrorType? = null,
    val errorMessage: String? = null
)
