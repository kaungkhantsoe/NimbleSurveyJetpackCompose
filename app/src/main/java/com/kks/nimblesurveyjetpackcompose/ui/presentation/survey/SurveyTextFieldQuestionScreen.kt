package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.SurveyText
import com.kks.nimblesurveyjetpackcompose.ui.theme.White30
import com.kks.nimblesurveyjetpackcompose.ui.theme.White40
import kotlinx.coroutines.delay

private const val TEXT_FIELD_HEIGHT = 56
private const val SPACE_BETWEEN_TEXT_FIELDS = 16

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SurveyTextFieldQuestionScreen(
    answers: List<SurveyAnswer>,
    onAnswerChange: (answers: List<SurveyAnswer>) -> Unit
) {
    var answerStates by remember { mutableStateOf(answers.map { it.answer }) }
    val heightOfQuestions = (TEXT_FIELD_HEIGHT * answers.size) + (SPACE_BETWEEN_TEXT_FIELDS * (answers.size - 1))
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = answerStates) {
        delay(DELAY_VALUE_CHANE_MILLI)
        onAnswerChange(answers.mapIndexed { index, surveyAnswer ->
            surveyAnswer.copy(answer = answerStates[index], selected = true)
        })
    }

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(heightOfQuestions.dp)) {
        answers.forEachIndexed { index, surveyAnswer ->
            TextField(
                value = answerStates[index],
                onValueChange = { text ->
                    answerStates = answerStates.toMutableList().also { it[index] = text }
                },
                shape = RoundedCornerShape(10.dp),
                placeholder = {
                    SurveyText(
                        text = surveyAnswer.text,
                        fontSize = 17.sp,
                        color = White40
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    backgroundColor = if (answerStates[index].isNotEmpty()) White40 else White30,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = if (index == answers.size - 1) ImeAction.Done else ImeAction.Next
                ),
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

