package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.SurveyText
import com.kks.nimblesurveyjetpackcompose.ui.theme.White20
import kotlinx.coroutines.delay

private const val DELAY_VALUE_CHANGE_MILLI = 300L

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

    TextField(
        value = answer,
        onValueChange = { answer = it },
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            SurveyText(
                text = answers.firstOrNull()?.text.orEmpty(),
                fontSize = 17.sp,
                color = White20
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            backgroundColor = White20,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
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
