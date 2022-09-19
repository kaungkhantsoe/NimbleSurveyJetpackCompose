package com.kks.nimblesurveyjetpackcompose.model

import com.kks.nimblesurveyjetpackcompose.model.request.SurveyQuestionRequest

data class SurveyQuestion(
    val id: String,
    val title: String,
    val displayOrder: Int,
    val shortText: String,
    val pick: String,
    val questionDisplayType: QuestionDisplayType,
    var answers: List<SurveyAnswer>
)

enum class QuestionDisplayType(val typeValue: String, val isIdOnlyAnswer: Boolean) {
    NONE("none", false),
    INTRO("intro", false),
    DROPDOWN("dropdown", true),
    SMILEY("smiley", true)
}

fun List<SurveyAnswer>.sortedByDisplayOrder(): List<SurveyAnswer> = this.sortedBy { it.displayOrder }

fun SurveyQuestion.toSurveyQuestionRequest(): SurveyQuestionRequest =
    SurveyQuestionRequest(
        id = id,
        answers = answers.filter { it.selected }.map {
            it.toSurveyAnswerRequest(isIdOnlyAnswer = questionDisplayType.isIdOnlyAnswer)
        }
    )

fun String.getQuestionDisplayType() =
    when (this) {
        QuestionDisplayType.INTRO.typeValue -> QuestionDisplayType.INTRO
        QuestionDisplayType.DROPDOWN.typeValue -> QuestionDisplayType.DROPDOWN
        QuestionDisplayType.SMILEY.typeValue -> QuestionDisplayType.SMILEY
        else -> QuestionDisplayType.NONE
    }
