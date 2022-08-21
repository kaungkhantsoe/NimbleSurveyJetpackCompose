package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.ui.theme.Concord
import com.kks.nimblesurveyjetpackcompose.ui.theme.White18
import com.kks.nimblesurveyjetpackcompose.util.extensions.loginTextFieldModifier
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination(start = true)
@Composable
fun SplashScreen(viewModel: SplashViewModel = viewModel()) {
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    var showLoginComponents by remember { mutableStateOf(false) }
    val logoOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val positionToAnimate = -LocalDensity.current.run { 221.dp.toPx() }

    LaunchedEffect(
        key1 = viewModel.shouldNavigateToLogin()
    ) {
        showLoginComponents = viewModel.shouldNavigateToLogin()
        if (showLoginComponents) {
            launch {
                logoOffset.animateTo(
                    Offset(logoOffset.value.x, positionToAnimate),
                    spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = StiffnessVeryLow
                    )
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = if (showLoginComponents) R.drawable.ic_overlay else R.drawable.splash_bg),
            contentDescription = stringResource(id = R.string.splash_background_content_description),
            modifier = Modifier
                .matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.ic_logo_white),
            contentDescription = stringResource(id = R.string.splash_logo_content_description),
            modifier = Modifier
                .size(201.0.dp, 48.0.dp)
                .offset {
                    IntOffset(
                        x = logoOffset.value.x.toInt(),
                        y = logoOffset.value.y.toInt()
                    )
                }
        )
        AnimatedVisibility(visible = showLoginComponents, enter = fadeIn()) {
            LoginComponents()
        }
    }
    viewModel.startTimerToNavigateToLogin()
}

@Suppress("LongMethod")
@Composable
fun LoginComponents() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        var enableLoginButton by remember { mutableStateOf(false) }
        var emailState by remember { mutableStateOf("") }
        var passwordState by remember { mutableStateOf("") }

        EmailTextField(
            emailState = emailState,
            onValueChange = {
                emailState = it
                enableLoginButton = emailState.isNotEmpty() && passwordState.isNotEmpty()
            }
        )
        PasswordTextField(
            passwordState = passwordState,
            onValueChange = {
                passwordState = it
                enableLoginButton = emailState.isNotEmpty() && passwordState.isNotEmpty()
            }
        )
        LoginButton(loginButtonState = enableLoginButton)
    }
}

@Composable
fun EmailTextField(emailState: String, onValueChange: (String) -> Unit) {
    TextField(
        value = emailState,
        onValueChange = {
            onValueChange(it)
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        label = { Text(LocalContext.current.getString(R.string.login_email)) },
        colors = textFieldColor(),
        modifier = Modifier.loginTextFieldModifier(),
        trailingIcon = {
            if (emailState.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
fun PasswordTextField(passwordState: String, onValueChange: (String) -> Unit) {
    val context = LocalContext.current

    TextField(
        value = passwordState,
        onValueChange = {
            onValueChange(it)
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        label = { Text(context.getString(R.string.login_password)) },
        colors = textFieldColor(),
        modifier = Modifier.loginTextFieldModifier(),
        trailingIcon = {
            TextButton(onClick = { }) {
                Text(context.getString(R.string.login_forget), color = Color.White)
            }
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun LoginButton(loginButtonState: Boolean) {
    Button(
        onClick = { },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier.loginTextFieldModifier(),
        enabled = loginButtonState
    ) {
        Text(
            LocalContext.current.getString(R.string.login_log_in),
            color = Color.Black,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun textFieldColor() =
    TextFieldDefaults.textFieldColors(
        backgroundColor = White18,
        cursorColor = Color.Black,
        disabledLabelColor = Concord,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedLabelColor = Color.Transparent,
        unfocusedLabelColor = Concord,
        textColor = Color.White
    )

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashScreen()
}
