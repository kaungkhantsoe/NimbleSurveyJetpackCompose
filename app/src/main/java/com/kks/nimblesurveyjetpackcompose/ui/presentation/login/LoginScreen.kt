package com.kks.nimblesurveyjetpackcompose.ui.presentation.login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun LoginScreen() {
    val activity = (LocalContext.current as? Activity)

    BackHandler {
        activity?.finish()
    }
}
