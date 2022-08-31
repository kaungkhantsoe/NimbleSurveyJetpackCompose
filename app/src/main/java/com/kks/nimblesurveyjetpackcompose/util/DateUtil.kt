package com.kks.nimblesurveyjetpackcompose.util

import java.text.SimpleDateFormat
import java.util.*

const val EEEE_MMM_D = "EEEE, MMM d"

object DateUtil {
    fun getBeautifiedDate(date: Date = Date()): String = SimpleDateFormat(EEEE_MMM_D, Locale.ENGLISH).format(date)
}
