package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kks.nimblesurveyjetpackcompose.R

@Composable
fun SurveyToolbar(
    modifier: Modifier = Modifier,
    showBack: Boolean,
    showClose: Boolean,
    onClickClose: () -> Unit,
    onPopBack: () -> Unit
) {
    Box(modifier = modifier) {
        if (showBack) {
            IconButton(
                onClick = { onPopBack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_accent),
                    contentDescription = stringResource(id = R.string.survey_detail_back_icon)
                )
            }
        }
        if (showClose) {
            IconButton(
                onClick = { onClickClose() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_close_button_white),
                    contentDescription = stringResource(id = R.string.survey_detail_close_icon),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun SurveyToolbarPreview() {
    SurveyToolbar(
        modifier = Modifier.fillMaxWidth(),
        showBack = true,
        showClose = true,
        onClickClose = { /* Do nothing*/ }) {}
}
