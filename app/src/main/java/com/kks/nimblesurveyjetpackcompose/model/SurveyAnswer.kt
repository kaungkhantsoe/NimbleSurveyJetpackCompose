package com.kks.nimblesurveyjetpackcompose.model

import com.kks.nimblesurveyjetpackcompose.model.request.SurveyAnswerRequest

data class SurveyAnswer(
    val id: String,
    val text: String,
    val displayOrder: Int,
    var selected: Boolean = false,
    var answer: String = ""
)

fun SurveyAnswer.toSurveyAnswerRequest(isIdOnlyAnswer: Boolean): SurveyAnswerRequest =
    if (isIdOnlyAnswer) SurveyAnswerRequest(id = id) else SurveyAnswerRequest(id = id, answer = answer)

fun List<SurveyAnswer>.sortedByDisplayOrder(): List<SurveyAnswer> = this.sortedBy { it.displayOrder }
