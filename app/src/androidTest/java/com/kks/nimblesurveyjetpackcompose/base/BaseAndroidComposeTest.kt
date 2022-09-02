package com.kks.nimblesurveyjetpackcompose.base

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.adevinta.android.barista.rule.flaky.FlakyTestRule
import com.kks.nimblesurveyjetpackcompose.ui.presentation.main.MainActivity
import org.junit.Rule

abstract class BaseAndroidComposeTest {
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val flakyRule = FlakyTestRule()

    protected fun getString(resId: Int) = composeTestRule.activity.getString(resId)
}
