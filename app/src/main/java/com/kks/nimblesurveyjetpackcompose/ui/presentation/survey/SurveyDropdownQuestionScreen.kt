package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.ui.theme.Black60

private const val INVALID_INDEX = -1

@Composable
fun SurveyDropDownQuestionScreen(answers: List<SurveyAnswer>, onChooseAnswer: (answers: List<SurveyAnswer>) -> Unit) {
    if (answers.isEmpty()) return
    val answerIndex = answers.indexOfFirst { it.selected }
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(if (answerIndex == INVALID_INDEX) 0 else answerIndex) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    LaunchedEffect(key1 = selectedIndex) {
        onChooseAnswer(
            answers.mapIndexed { index, surveyAnswer ->
                surveyAnswer.copy(selected = index == selectedIndex)
            }
        )
    }
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(shape = RoundedCornerShape(4.dp), color = Black60)
                .onGloballyPositioned { rowSize = it.size.toSize() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = answers[selectedIndex].text,
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(
                        id = if (expanded) android.R.drawable.arrow_up_float else android.R.drawable.arrow_down_float
                    ),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.size(width = with(LocalDensity.current) { rowSize.width.toDp() }, height = 150.dp),
        ) {
            answers.forEachIndexed { index, surveyAnswer ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedIndex = index
                    }
                ) {
                    Text(text = surveyAnswer.text)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SurveyDropDownQuestionPreview() {
    SurveyDropDownQuestionScreen(
        listOf(
            SurveyAnswer("", "Text1", 0, true, "answer1"),
            SurveyAnswer("", "Text2", 1, true, "answer2")
        )
    ) {
        // Do nothing
    }
}
