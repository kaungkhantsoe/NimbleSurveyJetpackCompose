package com.kks.nimblesurveyjetpackcompose.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyQuestionRequest(
    @Json(name = "id")
    val id: String,
    @Json(name = "answers")
    val answers: List<SurveyAnswerRequest>
)
