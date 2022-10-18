package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.theme.White50

private const val START_INDEX = 0
private const val MAX_NUMBER_OF_NPS_QUESTION = 10

@Composable
fun SurveyNpsQuestionScreen(
    answers: List<SurveyAnswer>, onChooseAnswer: (answers: List<SurveyAnswer>) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(answers.indexOfFirst { it.selected }) }

    ConstraintLayout(modifier = Modifier.wrapContentWidth()) {
        val (nps, notAtAll, extremely) = createRefs()
        Row(
            modifier = Modifier.constrainAs(nps) {
                start.linkTo(parent.start, 20.dp)
                end.linkTo(parent.end, 20.dp)
                top.linkTo(parent.top)
            }, horizontalArrangement = Arrangement.Center
        ) {
            answers.take(MAX_NUMBER_OF_NPS_QUESTION)
                .forEachIndexed { index, surveyAnswer ->
                val eachItemWidth = LocalDensity.current.run {
                    (LocalConfiguration.current.screenWidthDp.dp - 40.dp) / MAX_NUMBER_OF_NPS_QUESTION
                }
                TextButton(
                    onClick = {
                        selectedIndex = index
                        onChooseAnswer(answers.mapIndexed { index, surveyAnswer ->
                            surveyAnswer.copy(selected = index == selectedIndex)
                        })
                    },
                    border = BorderStroke(0.5.dp, Color.White),
                    shape = RoundedCornerShape(
                        topStart = if (index == START_INDEX) 10.dp else 0.dp,
                        bottomStart = if (index == START_INDEX) 10.dp else 0.dp,
                        topEnd = if (index == MAX_NUMBER_OF_NPS_QUESTION - 1) 10.dp else 0.dp,
                        bottomEnd = if (index == MAX_NUMBER_OF_NPS_QUESTION - 1) 10.dp else 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    modifier = Modifier.size(eachItemWidth, 56.dp)
                ) {
                    SurveyBoldText(
                        text = surveyAnswer.text, color = if (index <= selectedIndex) {
                            Color.White
                        } else {
                            White50
                        }, fontSize = 20.sp, textAlign = TextAlign.Center
                    )
                }
            }
        }
        SurveyBoldText(
            text = stringResource(id = R.string.survey_question_not_at_all_likely),
            fontSize = 17.sp,
            modifier = Modifier.constrainAs(notAtAll) {
                top.linkTo(nps.bottom)
                start.linkTo(nps.start)
            },
            color = White50
        )
        SurveyBoldText(text = stringResource(id = R.string.survey_question_extremely_likely),
            fontSize = 17.sp,
            modifier = Modifier.constrainAs(extremely) {
                top.linkTo(nps.bottom)
                end.linkTo(nps.end)
            })
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PreviewSurveyNPSQuestion() {
    SurveyNpsQuestionScreen(answers = emptyList()) {
        // Do nothing
    }
}
