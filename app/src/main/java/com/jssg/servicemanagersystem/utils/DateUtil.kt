package com.jssg.servicemanagersystem.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    val fullDateFormat: String = "yyyy-MM-dd HH:mm:ss"
    val yyyyMMddDateFormat: String = "yyyy-MM-dd"
    val FULL_DATE_FORMAT: DateFormat = SimpleDateFormat(fullDateFormat)
    val FORMAT_yyyy_MM_dd: DateFormat = SimpleDateFormat(yyyyMMddDateFormat)

    /**
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @param time
     * @return
     */
    fun getFullData(time: Long): String {
        return FULL_DATE_FORMAT.format(Date(time))
    }

    fun getDateyyMMdd(time: Long): String {
        return FORMAT_yyyy_MM_dd.format(Date(time))
    }

    fun formatDateString(fromDate: String, formatPattern: String): String? {
        var date: Date? = null
        try {
            date = FULL_DATE_FORMAT.parse(fromDate)
        } catch (e: ParseException) {
            LogUtil.e(DateUtil::class.java.simpleName, e.toString())
        }

        date?.let {
            return SimpleDateFormat(formatPattern).format(date)
        }
        return null
    }

    /**
     * startDate <= endDate return true
     * @param startDate String
     * @param endDate String
     * @return Boolean
     */
    fun compareDate(startDate: String, endDate: String): Boolean {
        var date1: Date? = null
        try {
            date1 = FULL_DATE_FORMAT.parse(startDate)
        } catch (e: ParseException) {
            LogUtil.e(DateUtil::class.java.simpleName, e.toString())
        }

        var date2: Date? = null
        try {
            date2 = FULL_DATE_FORMAT.parse(endDate)
        } catch (e: ParseException) {
            LogUtil.e(DateUtil::class.java.simpleName, e.toString())
        }

        return date1?.before(date2) == true || date1?.equals(date2) == true
    }
}