package com.kks.nimblesurveyjetpackcompose.util

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
