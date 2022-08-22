package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyDataResponse(
    @Json(name = "id")
    val id: String?,
    @Json(name = "type")
    val type: String?,
)
