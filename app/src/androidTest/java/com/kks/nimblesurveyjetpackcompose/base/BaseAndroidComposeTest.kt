package com.kks.nimblesurveyjetpackcompose.base

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kks.nimblesurveyjetpackcompose.ui.presentation.main.MainActivity
import org.junit.Rule

abstract class BaseAndroidComposeTest {
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    protected fun getString(resId: Int) = composeTestRule.activity.getString(resId)
}
