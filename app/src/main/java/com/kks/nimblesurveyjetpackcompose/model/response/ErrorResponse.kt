package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "source")
    val source: String?,
    @Json(name = "detail")
    val detail: String?,
    @Json(name = "code")
    val code: String?,
)
