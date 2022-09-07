package com.kks.nimblesurveyjetpackcompose.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.kks.nimblesurveyjetpackcompose.R

const val PREF_ACCESS_TOKEN = "access_token"
const val PREF_REFRESH_TOKEN = "refresh_token"

interface PreferenceManager {
    fun setStringData(key: String, value: String?)
    fun setIntegerData(key: String, value: Int)
    fun setLongData(key: String, value: Long)
    fun setBooleanData(key: String, value: Boolean)
    fun getStringData(key: String): String?
    fun getIntegerData(key: String): Int
    fun getLongData(key: String): Long
    fun getBooleanData(key: String): Boolean
    fun deleteAllData()
}
class PreferenceManagerImpl(context: Context): PreferenceManager {
    private val preferences: SharedPreferences? =
        context.getSharedPreferences(
            "pref_${context.getString(R.string.app_name)}",
            Context.MODE_PRIVATE
        )

    override fun setStringData(key: String, value: String?) {
        preferences?.edit { putString(key, value) }
    }

    override fun setIntegerData(key: String, value: Int) {
        preferences?.edit { putInt(key, value) }
    }

    override fun setLongData(key: String, value: Long) {
        preferences?.edit { putLong(key, value) }
    }

    override fun setBooleanData(key: String, value: Boolean) {
        preferences?.edit { putBoolean(key, value) }
    }

    override fun getStringData(key: String): String? {
        return preferences?.getString(key, "")
    }

    override fun getIntegerData(key: String): Int {
        return preferences?.getInt(key, -1) ?: 0
    }

    override fun getLongData(key: String): Long {
        return preferences?.getLong(key, -1L) ?: 0L
    }

    override fun getBooleanData(key: String): Boolean {
        return preferences?.getBoolean(key, false) ?: false
    }

    override fun deleteAllData() {
        preferences?.edit()?.clear()?.apply()
    }
}
