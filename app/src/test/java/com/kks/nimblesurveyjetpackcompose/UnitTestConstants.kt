package com.kks.nimblesurveyjetpackcompose

import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedAnswerResponse
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedQuestionResponse
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyDataResponse
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyIncludedRelationshipsResponse
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyQuestionsResponse

val surveyAnswer = SurveyAnswer(
    id = "0",
    text = "text",
    displayOrder = 0
)

val surveyQuestion = SurveyQuestion(
    id = "0",
    title = "title question",
    displayOrder = 0,
    shortText = "",
    pick = "none",
    questionDisplayType = QuestionDisplayType.NONE,
    answers = listOf(surveyAnswer)
)

val surveyQuestions = listOf(
    surveyQuestion,
    surveyQuestion.copy(id = "1", title = "title")
)

val surveyAnswerDataResponse = SurveyDataResponse(id = "0", type = "answer")

val attributeQuestionResponse =
    IncludedQuestionResponse.AttributeQuestionResponse(displayType = "intro", displayOrder = 0)

val includedQuestionResponse = IncludedQuestionResponse(
    id = "0",
    relationships = SurveyIncludedRelationshipsResponse(
        answers = SurveyQuestionsResponse(
            data = listOf(
                surveyAnswerDataResponse,
                surveyAnswerDataResponse.copy(id = "1")
            )
        )
    ),
    attributes = attributeQuestionResponse
)

val attributeAnswerResponse = IncludedAnswerResponse.AttributeAnswerResponse(displayOrder = 0)

val includedAnswerResponse = IncludedAnswerResponse(id = "0", attributes = attributeAnswerResponse)
