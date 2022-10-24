package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kks.nimblesurveyjetpackcompose.ui.theme.White40

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SurveyTextField(
    value: String,
    onValueChange: (text: String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = White40,
    imeAction: ImeAction = ImeAction.Default
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = value,
        onValueChange = { text ->
            onValueChange(text)
        },
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            SurveyText(
                text = placeholderText,
                fontSize = 17.sp,
                color = White40
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            backgroundColor = backgroundColor,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { keyboardController?.hide() }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        modifier = modifier
    )
}
