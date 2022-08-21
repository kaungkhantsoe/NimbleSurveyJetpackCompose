package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomErrorResponse(
    @Json(name = "errors")
    val errors: List<ErrorResponse>?,
)
