package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily

@Composable
fun SurveyBoldText(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        fontFamily = NeuzeitFamily,
        fontWeight = FontWeight.Bold,
        color = color,
        fontSize = fontSize,
        maxLines = maxLines,
        modifier = modifier,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun SurveyBoldTextPreview() {
    SurveyBoldText(text = "Text", fontSize = 24.sp)
}
