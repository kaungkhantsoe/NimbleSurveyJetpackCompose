package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.DROPDOWN
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.NONE
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.SMILEY
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.THUMBS
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.model.sortedByDisplayOrder
import com.kks.nimblesurveyjetpackcompose.ui.theme.White50

private const val NUMBER_OF_EMOJI_ANSWERS = 5

@Composable
fun SurveyQuestionScreen(
    surveyQuestion: SurveyQuestion,
    pageNumber: Int,
    totalNumberOfPage: Int,
    onChooseAnswer: (questionId: String, surveyAnswers: List<SurveyAnswer>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.survey_question_number,
                pageNumber,
                totalNumberOfPage
            ),
            fontSize = 15.sp,
            color = White50
        )
        SurveyBoldText(text = surveyQuestion.title, fontSize = 34.sp)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (surveyQuestion.questionDisplayType) {
                DROPDOWN -> SurveyDropDownQuestion(answers = surveyQuestion.answers.sortedByDisplayOrder()) {
                    onChooseAnswer(surveyQuestion.id, it)
                }
                SMILEY,
                THUMBS -> if (surveyQuestion.answers.size >= NUMBER_OF_EMOJI_ANSWERS) {
                    SurveyEmojiQuestion(
                        answers = surveyQuestion.answers.sortedByDisplayOrder(),
                        questionDisplayType = surveyQuestion.questionDisplayType
                    ) {
                        onChooseAnswer(surveyQuestion.id, it)
                    }
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SurveyQuestionScreenPreview() {
    SurveyQuestionScreen(
        surveyQuestion = SurveyQuestion(
            id = "",
            title = "",
            displayOrder = 0,
            shortText = "",
            pick = "",
            questionDisplayType = NONE,
            answers = emptyList()
        ),
        pageNumber = 1,
        totalNumberOfPage = 5
    ) { _, _ ->
        // Do nothing
    }
}

