package com.kks.nimblesurveyjetpackcompose.util.extensions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.loginTextFieldModifier() = this
    .fillMaxWidth()
    .padding(start = 24.dp, end = 24.dp)
    .height(56.dp)
