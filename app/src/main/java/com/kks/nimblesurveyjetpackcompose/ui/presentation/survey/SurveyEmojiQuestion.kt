package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.theme.Black50

private val SMILEY_EMOJIS = listOf("😡", "😕", "😐", "🙂", "😄")
private val THUMBS_EMOJIS = listOf("\uD83D\uDC4D", "\uD83D\uDC4D", "\uD83D\uDC4D", "\uD83D\uDC4D", "\uD83D\uDC4D")

@Composable
fun SurveyEmojiQuestion(
    answers: List<SurveyAnswer>,
    questionDisplayType: QuestionDisplayType,
    onChooseAnswer: (answers: List<SurveyAnswer>) -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        when (questionDisplayType) {
            QuestionDisplayType.SMILEY -> SMILEY_EMOJIS
            QuestionDisplayType.THUMBS -> THUMBS_EMOJIS
            else -> listOf()
        }.forEachIndexed { index, text ->
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
    SurveyEmojiQuestion(answers = listOf(), questionDisplayType = QuestionDisplayType.SMILEY) {
        // Do nothing
    }
}