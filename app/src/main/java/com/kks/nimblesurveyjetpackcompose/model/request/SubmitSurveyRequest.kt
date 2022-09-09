package com.kks.nimblesurveyjetpackcompose.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubmitSurveyRequest(
    @Json(name = "survey_id")
    val surveyId: String,
    @Json(name = "questions")
    val questions: List<SurveyQuestionRequest>
)
