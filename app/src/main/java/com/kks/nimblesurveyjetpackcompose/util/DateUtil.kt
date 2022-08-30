package com.kks.nimblesurveyjetpackcompose.util

import java.text.SimpleDateFormat
import java.util.*

const val EEEE_MMM_D = "EEEE, MMM d"

object DateUtil {

    private val EEEE_comma_MMMMM_d =
        SimpleDateFormat(EEEE_MMM_D, Locale.ENGLISH)

    fun getBeautifiedDate(date: Date = Date()): String = EEEE_comma_MMMMM_d.format(date)
}
