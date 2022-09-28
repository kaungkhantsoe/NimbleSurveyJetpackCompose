package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SurveyNpsQuestion(
    answers: List<SurveyAnswer>,
    onChooseAnswer: (answers: List<SurveyAnswer>) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(answers.indexOfFirst { it.selected }) }

    ConstraintLayout(modifier = Modifier.wrapContentWidth()) {
        val (nps, notAtAll, extremely) = createRefs()
        Row(
            modifier = Modifier
                .constrainAs(nps) {
                    start.linkTo(parent.start, 20.dp)
                    end.linkTo(parent.end, 20.dp)
                    top.linkTo(parent.top)
                },
            horizontalArrangement = Arrangement.Center
        ) {
            answers.forEachIndexed { index, surveyAnswer ->
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        onClick = {
                            selectedIndex = index
                            onChooseAnswer(
                                answers.mapIndexed { index, surveyAnswer ->
                                    surveyAnswer.copy(selected = index == selectedIndex)
                                }
                            )
                        },
                        border = BorderStroke(0.5.dp, Color.White),
                        shape = RoundedCornerShape(
                            topStart = if (index == START_INDEX) 10.dp else 0.dp,
                            bottomStart = if (index == START_INDEX) 10.dp else 0.dp,
                            topEnd = if (index == answers.lastIndex) 10.dp else 0.dp,
                            bottomEnd = if (index == answers.lastIndex) 10.dp else 0.dp
                        ),
                        color = Color.Transparent,
                        modifier = Modifier.size(33.dp, 56.dp)
                    ) { /* Do nothing */ }

                    SurveyBoldText(
                        text = surveyAnswer.text,
                        color = if (index <= selectedIndex) {
                            Color.White
                        } else {
                            White50
                        },
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
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
        SurveyBoldText(
            text = stringResource(id = R.string.survey_question_extremely_likely),
            fontSize = 17.sp,
            modifier = Modifier.constrainAs(extremely) {
                top.linkTo(nps.bottom)
                end.linkTo(nps.end)
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PreviewSurveyNPSQuestion() {
    SurveyNpsQuestion(answers = emptyList()) {
        // Do nothing
    }
}
