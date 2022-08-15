package com.kks.nimblesurveyjetpackcompose.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class CustomKeyGenerator(applicationContext: Context) : CustomKeyProvider {
    private val applicationInfo: ApplicationInfo = applicationContext.packageManager
        .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
    private val uid = applicationInfo.metaData["uid"]
    private val secret = applicationInfo.metaData["secret"]

    override fun getClientId(): String = uid.toString()

    override fun getClientSecret(): String = secret.toString()
}
