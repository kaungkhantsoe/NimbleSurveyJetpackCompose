package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class BaseIncludedResponse(@Json(name = "type") val type: String?)

enum class IncludedType {
    ANSWER, QUESTION
}
