package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.ui.theme.White50

@Composable
fun SurveyQuestionScreen(surveyQuestion: SurveyQuestion) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(
                id = R.string.survey_question_number,
                surveyQuestion.pageNumber,
                surveyQuestion.totalNumberOfPage
            ),
            fontSize = 15.sp,
            color = White50,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        SurveyTitleText(text = surveyQuestion.title, fontSize = 34.sp, modifier = Modifier.padding(horizontal = 20.dp))
    }
}

@Preview
@Composable
fun SurveyQuestionScreenPreview() {
    SurveyQuestionScreen(SurveyQuestion(pageNumber = 1, totalNumberOfPage = 5, title = "Title"))
}
