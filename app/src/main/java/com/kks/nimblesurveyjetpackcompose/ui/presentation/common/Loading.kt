package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kks.nimblesurveyjetpackcompose.ui.theme.CuriousBlue

@Composable
fun Loading() {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        CircularProgressIndicator(
            color = CuriousBlue,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    Loading()
}
