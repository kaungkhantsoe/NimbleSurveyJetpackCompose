package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.SurveyText
import com.kks.nimblesurveyjetpackcompose.ui.theme.White20
import com.kks.nimblesurveyjetpackcompose.ui.theme.White30

@Composable
fun SurveyTextAreaQuestionScreen(
    answers: List<SurveyAnswer>,
    onChooseAnswer: (answers: List<SurveyAnswer>) -> Unit
) {
    var answer by remember { mutableStateOf("") }

    TextField(
        value = answer,
        onValueChange = {
            answer = it
        },
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            SurveyText(
                text = stringResource(id = R.string.survey_question_your_thoughts),
                fontSize = 17.sp,
                color = White30
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
            .onFocusChanged {
                if (!it.isFocused) {
                    onChooseAnswer(answers.map { surveyAnswer -> surveyAnswer.copy(text = answer, selected = true) })
                }
            }
    )
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun SurveyTextAreaQuestionScreenPreview() {
    SurveyTextAreaQuestionScreen(emptyList(), {})
}
