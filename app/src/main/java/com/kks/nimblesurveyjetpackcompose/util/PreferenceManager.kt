package com.kks.nimblesurveyjetpackcompose.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.kks.nimblesurveyjetpackcompose.R

const val PREF_ACCESS_TOKEN = "access_token"
const val PREF_REFRESH_TOKEN = "refresh_token"

class PreferenceManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(
            "pref_${context.getString(R.string.app_name)}",
            Context.MODE_PRIVATE
        )

    fun setStringData(key: String, value: String?) {
        preferences.edit { putString(key, value) }
    }

    fun setIntegerData(key: String, value: Int) {
        preferences.edit { putInt(key, value) }
    }

    fun setLongData(key: String, value: Long) {
        preferences.edit { putLong(key, value) }
    }

    fun setBooleanData(key: String, value: Boolean) {
        preferences.edit { putBoolean(key, value) }
    }

    fun getStringData(key: String): String? {
        return preferences.getString(key, "")
    }

    fun getIntegerData(key: String): Int {
        return preferences.getInt(key, -1)
    }

    fun getLongData(key: String): Long {
        return preferences.getLong(key, -1L)
    }

    fun getBooleanData(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun deleteAllData() {
        return preferences.edit().clear().apply()
    }
}
