package com.kks.nimblesurveyjetpackcompose.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyAnswerRequest(
    @Json(name = "id")
    val id: String,
    @Json(name = "answer")
    val answer: String? = null
)
