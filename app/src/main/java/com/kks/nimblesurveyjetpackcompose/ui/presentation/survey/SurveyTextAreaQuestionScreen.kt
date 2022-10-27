package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.SurveyTextField
import com.kks.nimblesurveyjetpackcompose.ui.theme.White30
import com.kks.nimblesurveyjetpackcompose.ui.theme.White40
import kotlinx.coroutines.delay

const val DELAY_VALUE_CHANGE_MILLI = 300L

@Composable
fun SurveyTextAreaQuestionScreen(
    answers: List<SurveyAnswer>,
    onAnswerChange: (answers: List<SurveyAnswer>) -> Unit
) {
    var answer by remember { mutableStateOf(answers.first().answer) }

    LaunchedEffect(key1 = answer) {
        delay(DELAY_VALUE_CHANGE_MILLI)
        onAnswerChange(answers.map { surveyAnswer -> surveyAnswer.copy(answer = answer, selected = true) })
    }

    SurveyTextField(
        value = answer,
        onValueChange = { answer = it },
        placeholderText = answers.first().text,
        backgroundColor = if (answer.isNotEmpty()) White40 else White30,
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun SurveyTextAreaQuestionScreenPreview() {
    SurveyTextAreaQuestionScreen(
        listOf(SurveyAnswer("", "Your thought", 0, false))
    ) {}
}
