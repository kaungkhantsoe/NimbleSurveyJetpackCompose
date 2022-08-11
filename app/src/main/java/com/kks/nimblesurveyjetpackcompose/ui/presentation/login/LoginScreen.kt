package com.kks.nimblesurveyjetpackcompose.ui.presentation.login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun LoginScreen() {
    val activity = (LocalContext.current as? Activity)

    // TODO: Remove this code when implementing UI for Login at https://github.com/kaungkhantsoe/NimbleSurveyJetpackCompose/issues/15
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Login")
    }

    BackHandler {
        activity?.finish()
    }
}
