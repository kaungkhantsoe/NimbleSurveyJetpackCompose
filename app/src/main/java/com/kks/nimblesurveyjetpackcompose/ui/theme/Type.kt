package com.kks.nimblesurveyjetpackcompose.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.kks.nimblesurveyjetpackcompose.R

val NeuzeitFamily = FontFamily(
    Font(R.font.neuzeit_heavy, FontWeight.Bold),
    Font(R.font.neuzeit_book, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = NeuzeitFamily,
)
