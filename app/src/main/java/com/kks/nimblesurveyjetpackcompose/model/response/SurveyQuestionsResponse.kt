package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyQuestionsResponse(
    @Json(name = "data")
    val data: List<SurveyDataResponse>?
)
