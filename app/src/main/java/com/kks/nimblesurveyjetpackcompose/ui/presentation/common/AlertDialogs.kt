package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.layout.*
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
        onDismissRequest = { onClickButton() },
        title = { Text(text = title) },
        text = { Text(text = errorModel.errorMessage.orEmpty()) },
        backgroundColor = Color.White,
        confirmButton = {
            Box(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = { onClickButton() }) {
                    Text(text = buttonText, color = CuriousBlue)
                }
            }
        }
    )
}

@Composable
fun ConfirmAlertDialog(
    title: String,
    message: String,
    positiveBtnText: String,
    negativeBtnText: String,
    onClickPositiveBtn: () -> Unit,
    onClickNegativeBtn: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClickPositiveBtn() },
        title = { Text(text = title) },
        text = { Text(text = message) },
        backgroundColor = Color.White,
        confirmButton = {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onClickNegativeBtn() }) {
                    Text(text = negativeBtnText, color = CuriousBlue)
                }
                TextButton(onClick = { onClickPositiveBtn() }) {
                    Text(text = positiveBtnText, color = CuriousBlue)
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
        buttonText = "OK",
        onClickButton = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmAlertDialogPreview() {
    ConfirmAlertDialog(
        message = "Confirm",
        title = "Title",
        positiveBtnText = "OK",
        negativeBtnText = "Cancel",
        onClickPositiveBtn = {},
        onClickNegativeBtn = {},
    )
}
