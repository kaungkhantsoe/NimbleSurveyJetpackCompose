package com.kks.nimblesurveyjetpackcompose.model

data class SurveyQuestion(
    val id: String,
    val title: String,
    val displayOrder: Int,
    val shortText: String,
    val pick: String,
    val questionDisplayType: QuestionDisplayType,
    var answers: List<SurveyAnswer>
)

enum class QuestionDisplayType(val typeValue: String) {
    NONE("none"), INTRO("intro"), DROPDOWN("dropdown")
}

fun String.getQuestionDisplayType() =
    when (this) {
        QuestionDisplayType.INTRO.typeValue -> QuestionDisplayType.INTRO
        QuestionDisplayType.DROPDOWN.typeValue -> QuestionDisplayType.DROPDOWN
        else -> QuestionDisplayType.NONE
    }
