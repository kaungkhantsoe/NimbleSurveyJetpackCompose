package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestionPickType
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily
import com.kks.nimblesurveyjetpackcompose.ui.theme.White50

@Composable
fun SurveyChoiceQuestionScreen(
    answers: List<SurveyAnswer>,
    pickType: SurveyQuestionPickType,
    onChooseAnswer: (answers: List<SurveyAnswer>) -> Unit
) {
    val selectedIndexList = remember {
        mutableStateListOf<Int>().also { snapShot ->
            answers.forEachIndexed { index, surveyAnswer ->
                if (surveyAnswer.selected) snapShot.add(index)
            }
        }
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 70.dp)) {
        answers.forEachIndexed { index, surveyAnswer ->
            Box {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            if (selectedIndexList.contains(index)) {
                                selectedIndexList.remove(index)
                            } else {
                                if (pickType == SurveyQuestionPickType.SINGLE) selectedIndexList.clear()
                                selectedIndexList.add(index)

                            }
                            onChooseAnswer(answers.mapIndexed { index, surveyAnswer ->
                                surveyAnswer.copy(selected = selectedIndexList.contains(index))
                            })
                        }
                    ) {
                        val isSelected = selectedIndexList.contains(index)
                        Text(
                            text = surveyAnswer.text,
                            fontFamily = NeuzeitFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else White50,
                            fontSize = 20.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 10.dp).weight(.75f)
                        )
                        Surface(
                            border = BorderStroke(0.5.dp, if (isSelected) Color.White else White50),
                            shape = CircleShape,
                            modifier = Modifier.size(25.dp),
                            color = if (isSelected) Color.White else Color.Transparent
                        ) {
                            if (isSelected) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_check),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(5.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                if (index != answers.lastIndex) {
                    Divider(
                        color = Color.White,
                        thickness = 0.5.dp,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PreviewSurveyChoiceQuestionScreen() {
    SurveyChoiceQuestionScreen(
        answers = listOf(
            SurveyAnswer("", "Choice 1", 0, false),
            SurveyAnswer("", "Choice 2", 1, true)
        ),
        pickType = SurveyQuestionPickType.SINGLE
    ) {
        // Do nothing
    }
}
