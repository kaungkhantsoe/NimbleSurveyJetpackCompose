package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kks.nimblesurveyjetpackcompose.ui.theme.CuriousBlue
import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType

@Composable
fun ErrorAlertDialog(
    errorState: Pair<ErrorType, String>,
    title: String,
    buttonText: String,
    onClickButton: () -> Unit,
) {
    if (errorState.first != ErrorType.NONE) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { onClickButton },
            title = {
                Column {
                    Text(text = title)
                    Divider(
                        color = CuriousBlue,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            text = { Text(text = errorState.second) },
            backgroundColor = Color.White,
            buttons = {
                Box(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        onClick = { onClickButton() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = CuriousBlue),
                    ) {
                        Text(text = buttonText, color = Color.White)
                    }
                }
            }
        )
    }
}
