package com.kks.nimblesurveyjetpackcompose.base

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kks.nimblesurveyjetpackcompose.ui.presentation.main.MainActivity
import org.junit.Rule

abstract class BaseAndroidComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    fun getString(resId: Int) = composeTestRule.activity.getString(resId)
}
