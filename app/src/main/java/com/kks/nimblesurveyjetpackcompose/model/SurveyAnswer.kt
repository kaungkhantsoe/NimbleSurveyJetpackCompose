package com.kks.nimblesurveyjetpackcompose.model

import com.kks.nimblesurveyjetpackcompose.model.request.SurveyAnswerRequest

data class SurveyAnswer(
    val id: String,
    var text: String,
    val displayOrder: Int,
    var selected: Boolean = false,
    var isIdOnlyAnswer: Boolean = false
)

fun SurveyAnswer.toSurveyAnswerRequest(): SurveyAnswerRequest =
    if (isIdOnlyAnswer) SurveyAnswerRequest(id = id) else SurveyAnswerRequest(id = id, answer = text)
