package com.kks.nimblesurveyjetpackcompose.model

import com.kks.nimblesurveyjetpackcompose.model.request.SurveyAnswerRequest

data class SurveyAnswer(
    val id: String,
    var text: String,
    val displayOrder: Int,
    var selected: Boolean = false
)

fun SurveyAnswer.toSurveyAnswerRequest(isIdOnlyAnswer: Boolean): SurveyAnswerRequest =
    if (isIdOnlyAnswer) SurveyAnswerRequest(id = id) else SurveyAnswerRequest(id = id, answer = text)

fun List<SurveyAnswer>.sortedByDisplayOrder(): List<SurveyAnswer> = this.sortedBy { it.displayOrder }
