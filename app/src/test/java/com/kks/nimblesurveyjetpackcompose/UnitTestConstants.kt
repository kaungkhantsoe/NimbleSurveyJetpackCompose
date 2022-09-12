package com.kks.nimblesurveyjetpackcompose

import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion

val surveyQuestion = SurveyQuestion(
    id = "0",
    title = "title question",
    displayOrder = 0,
    shortText = "",
    pick = "none",
    questionDisplayType = QuestionDisplayType.NONE,
    answers = listOf(
        SurveyAnswer(
            id = "0",
            text = "text",
            displayOrder = 0
        )
    )
)
val surveyQuestions = listOf(
    surveyQuestion,
    surveyQuestion.copy(id = "1", title = "title")
)
