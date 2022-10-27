package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.SurveyTextField
import com.kks.nimblesurveyjetpackcompose.ui.theme.White30
import com.kks.nimblesurveyjetpackcompose.ui.theme.White40
import kotlinx.coroutines.delay

@Composable
fun SurveyTextFieldQuestionScreen(
    answers: List<SurveyAnswer>,
    onAnswerChange: (answers: List<SurveyAnswer>) -> Unit
) {
    var answerState by remember { mutableStateOf(answers.map { it.answer }) }

    LaunchedEffect(key1 = answerState) {
        delay(DELAY_VALUE_CHANGE_MILLI)
        onAnswerChange(answers.mapIndexed { index, surveyAnswer ->
            surveyAnswer.copy(answer = answerState[index], selected = true)
        })
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        answers.forEachIndexed { index, surveyAnswer ->
            SurveyTextField(
                value = answerState[index],
                onValueChange = { text ->
                    answerState = answerState.toMutableList().also { it[index] = text }
                },
                placeholderText = surveyAnswer.text,
                backgroundColor = if (answerState[index].isNotEmpty()) White40 else White30,
                imeAction = if (index == answers.lastIndex) ImeAction.Done else ImeAction.Next,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun SurveyTextFieldQuestionScreenPreview() {
    SurveyTextFieldQuestionScreen(
        listOf(
            SurveyAnswer("", "Your thought", 0, false),
            SurveyAnswer("", "Your thought", 1, false)
        )
    ) {}
}

