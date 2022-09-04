package com.kks.nimblesurveyjetpackcompose

import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion

val surveys = listOf(
    Survey(id = "1", coverImagePlaceholderUrl = "", title = "title1", description = "description1"),
    Survey(id = "2", coverImagePlaceholderUrl = "", title = "title2", description = "description2")
)

val surveyQuestions = listOf(
    SurveyQuestion(
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
    ),
    SurveyQuestion(
        id = "1",
        title = "title",
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
)
