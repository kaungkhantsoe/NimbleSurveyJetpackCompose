package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyRelationshipsResponse(
    @Json(name = "questions")
    val questions: SurveyQuestionsResponse?
)
