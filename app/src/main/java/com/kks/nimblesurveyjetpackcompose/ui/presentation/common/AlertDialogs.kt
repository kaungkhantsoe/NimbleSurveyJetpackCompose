package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kks.nimblesurveyjetpackcompose.model.ErrorModel
import com.kks.nimblesurveyjetpackcompose.ui.theme.CuriousBlue
import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType

@Composable
fun ErrorAlertDialog(
    errorModel: ErrorModel,
    title: String,
    buttonText: String,
    onClickButton: () -> Unit,
) {
    AlertDialog(
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = { onClickButton() },
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
        text = { Text(text = errorModel.errorMessage.orEmpty()) },
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

@Preview(showBackground = true)
@Composable
fun ErrorAlertDialogPreview() {
    ErrorAlertDialog(
        errorModel = ErrorModel(ErrorType.INFO, "Error"),
        title = "Error Title",
        buttonText = "Ok",
        onClickButton = {}
    )
}
