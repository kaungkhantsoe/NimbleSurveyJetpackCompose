package com.kks.nimblesurveyjetpackcompose.util

import java.text.SimpleDateFormat
import java.util.*

const val EEEE_comma_MMMMM_d_format = "EEEE,MMMM d"

object DateUtil {

    private val EEEE_comma_MMMMM_d =
        SimpleDateFormat(EEEE_comma_MMMMM_d_format, Locale.ENGLISH)

    fun getBeautifiedCurrentDate(): String = EEEE_comma_MMMMM_d.format(Date())
}
