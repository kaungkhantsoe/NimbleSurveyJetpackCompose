package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.*
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestionPickType
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.SurveyText
import com.kks.nimblesurveyjetpackcompose.ui.theme.White50
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val NUMBER_OF_EMOJI_ANSWERS = 5
private const val DELAY_TO_CLEAR_FOCUS = 500L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SurveyQuestionScreen(
    surveyQuestion: SurveyQuestion,
    pageNumber: Int,
    totalNumberOfPage: Int,
    onChooseAnswer: (questionId: String, surveyAnswers: List<SurveyAnswer>) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    // Need to wait for keyboard hiding before clearFocus. If not the screen doesn't resize.
                    coroutineScope.launch {
                        delay(DELAY_TO_CLEAR_FOCUS)
                        focusManager.clearFocus()
                    }
                })
            }
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
        SurveyText(
            text = surveyQuestion.title,
            fontSize = 34.sp,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (surveyQuestion.questionDisplayType) {
                CHOICE -> SurveyChoiceQuestionScreen(answers = surveyQuestion.answers, pickType = surveyQuestion.pick) {
                    onChooseAnswer(surveyQuestion.id, it)
                }
                NPS -> SurveyNpsQuestionScreen(answers = surveyQuestion.answers) {
                    onChooseAnswer(surveyQuestion.id, it)
                }
                DROPDOWN -> SurveyDropDownQuestionScreen(answers = surveyQuestion.answers) {
                    onChooseAnswer(surveyQuestion.id, it)
                }
                SMILEY,
                STARS,
                THUMBS -> if (surveyQuestion.answers.size >= NUMBER_OF_EMOJI_ANSWERS) {
                    SurveyEmojiQuestionScreen(
                        answers = surveyQuestion.answers,
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

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun SurveyQuestionScreenPreview() {
    SurveyQuestionScreen(
        surveyQuestion = SurveyQuestion(
            id = "",
            title = "Title",
            displayOrder = 0,
            shortText = "",
            pick = SurveyQuestionPickType.SINGLE,
            questionDisplayType = NONE,
            answers = emptyList()
        ),
        pageNumber = 1,
        totalNumberOfPage = 5
    ) { _, _ ->
        // Do nothing
    }
}

