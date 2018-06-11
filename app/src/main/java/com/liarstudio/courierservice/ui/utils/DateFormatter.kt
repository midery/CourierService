package com.liarstudio.courierservice.ui.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    const val DATE_FORMAT_FULL = "dd/MM/yyyy"
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT_FULL, Locale.getDefault())

    fun parseDate(date: String): Date = simpleDateFormat.parse(date)

    fun parseCalendar(date: String): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = parseDate(date)
        return calendar
    }

    fun toString(date: Date): String = simpleDateFormat.format(date)

    fun toString(calendar: Calendar) = toString(calendar.time)

    fun today() = Calendar.getInstance()

    fun tomorrow() : Calendar {
        val cal = today()
        cal.add(Calendar.DATE, 1)
        return cal
    }
}