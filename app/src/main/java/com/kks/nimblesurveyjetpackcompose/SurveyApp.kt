package com.kks.nimblesurveyjetpackcompose

import android.app.Application
import com.kks.nimblesurveyjetpackcompose.util.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SurveyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimber()
    }

    @Suppress("ImplicitDefaultLocale")
    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "Class:%s: Line: %s, Method: %s",
                        super.createStackElementTag(element),
                        element.lineNumber,
                        element.methodName
                    )
                }
            })
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}
