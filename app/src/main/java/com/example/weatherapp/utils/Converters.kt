package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private lateinit var simpleHourFormat:SimpleDateFormat
private lateinit var simpleDateFormat:SimpleDateFormat
private lateinit var simpleDayFormat:SimpleDateFormat

fun getHourString(time: Int): String {
    simpleHourFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return simpleHourFormat.format(time * 1000L)


}

fun getDateString(time: Int): String {
    //simpleDateFormat = SimpleDateFormat("dd MMMM, h:mm a", Locale.ENGLISH)
    val date = Date(time * 1000L)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,  Locale.ENGLISH)
}
