package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kks.nimblesurveyjetpackcompose.R
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
        title = {
            Text(
                text = title,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = if (errorModel.errorType == ErrorType.NETWORK) {
                    stringResource(id = R.string.network_error)
                } else {
                    errorModel.errorMessage.orEmpty()
                },
                color = Color.Black
            )
        },
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
    positiveButtonText: String,
    negativeButtonText: String,
    onClickPositiveButton: () -> Unit,
    onClickNegativeButton: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClickPositiveButton() },
        title = {
            Text(
                text = title,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = message,
                color = Color.Black
            )
        },
        backgroundColor = Color.White,
        confirmButton = {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onClickNegativeButton() }) {
                    Text(text = negativeButtonText, color = CuriousBlue)
                }
                TextButton(onClick = { onClickPositiveButton() }) {
                    Text(text = positiveButtonText, color = CuriousBlue)
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
        positiveButtonText = "OK",
        negativeButtonText = "Cancel",
        onClickPositiveButton = {},
        onClickNegativeButton = {},
    )
}
