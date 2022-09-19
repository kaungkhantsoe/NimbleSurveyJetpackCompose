package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.theme.Black50

private val SMILEY_EMOJIS = listOf("😡", "😕", "😐", "🙂", "😄")

@Composable
fun SurveySmileyQuestion(answers: List<SurveyAnswer>, onChooseAnswer: (answers: List<SurveyAnswer>) -> Unit) {
    var selectedIndex by remember { mutableStateOf(-1) }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        SMILEY_EMOJIS.forEachIndexed { index, text ->
            TextButton(onClick = {
                selectedIndex = index
                onChooseAnswer(
                    answers.mapIndexed { index, surveyAnswer ->
                        surveyAnswer.copy(selected = index == selectedIndex)
                    }
                )
            }) {
                Text(
                    text = text,
                    color = if (selectedIndex == index) Color.Unspecified else Black50,
                    fontSize = 28.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSurveySmileyQuestion() {
    SurveySmileyQuestion(listOf()) {
        // Do nothing
    }
}
