package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private lateinit var simpleHourFormat:SimpleDateFormat
fun getHourString(time: Int): String {
    simpleHourFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return simpleHourFormat.format(time * 1000L)


}

fun getDateString(time: Int): String {
    //simpleDateFormat = SimpleDateFormat("dd MMMM, h:mm a", Locale.ENGLISH)
    val date = Date(time * 1000L)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK,
        Calendar.LONG,  Locale.ENGLISH)
}
fun convertTimeToLong(time:String):Long{
    val format = SimpleDateFormat("hh:mm a")
    return format.parse(time).time
}
fun convertDateToLong(date:String): Long {
    val format=SimpleDateFormat("dd MMM, yyyy")
    return format.parse(date).time
}
fun getWeekDay(context: Context, time: Int):String {
    val day = getDateString(time)
    var fullDayName = ""

    if (PreferenceManager.getSelectedLanguage(context) == Constants.LANGUAGE_AR) {
        when (day) {
            "Saturday" -> fullDayName = "السبت"
            "Sunday" -> fullDayName = "الاحد"
            "Monday" -> fullDayName = "الاثنين"
            "Tuesday" -> fullDayName = "الثلاثاء"
            "Wednesday" -> fullDayName = "الاربعاء"
            "Thursday" -> fullDayName = "الخميس"
            "Friday" -> fullDayName = "الجمعة"
        }}else{
            return day
    }
        return fullDayName
    }

fun getDateToAlert(timestamp: Long, language: String): String{
    return SimpleDateFormat("dd MMM, yyyy",Locale(language)).format(timestamp)
}
fun getTimeToAlert(timestamp: Long, language: String): String{
    return SimpleDateFormat("h:mm a",Locale(language)).format(timestamp)
}
//private fun isNetworkAvailable(): Boolean {
//    val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
//    return activeNetworkInfo != null && activeNetworkInfo.isConnected
//}
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
        .isConnected
}
