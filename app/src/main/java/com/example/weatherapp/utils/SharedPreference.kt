package com.example.weatherapp.utils

import android.content.Context
import com.example.weatherapp.R

object PreferenceManager {

    private const val PREF_NAME = "settings"
    private const val KEY_SELECTED_LANGUAGE = "selectedLanguage"
    private const val KEY_SELECTED_TEMPERATURE_UNIT = "selectedTemperatureUnit"
    private const val KEY_SELECTED_LOCATION = "selectedLocation"
    private const val DEFAULT_LANGUAGE = "English"
    private const val DEFAULT_TEMPERATURE_UNIT = "Celsius"
    private const val DEFAULT_LOCATION = "None"

    fun saveSelectedLanguage(context: Context, language: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SELECTED_LANGUAGE, language)
        editor.apply()
    }

    fun saveSelectedTemperatureUnit(context: Context, temperatureUnit: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SELECTED_TEMPERATURE_UNIT, temperatureUnit)
        editor.apply()
    }
    fun getSelectedTemperatureUnit(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_SELECTED_TEMPERATURE_UNIT, Constants.UNITS_CELSIUS) ?: Constants.UNITS_CELSIUS
    }


    fun getSelectedLanguage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_SELECTED_LANGUAGE, Constants.LANGUAGE_EN) ?: Constants.LANGUAGE_EN
    }


    fun saveSelectedLocation(context: Context, location: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SELECTED_LOCATION, location)
        editor.apply()
    }
    fun getSelectedLocation(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_SELECTED_LOCATION, Constants.LOCATION_GPS) ?: DEFAULT_LOCATION
    }
    fun setAppLocationByMap(context: Context,long:String,lat:String){
        val  sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.MAP_LON, long)
        editor.putString(Constants.MAP_LAT, lat)
        editor.apply()
    }

    fun getAppLocationByMap(context: Context): Pair<String?, String?> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return Pair(sharedPreferences.getString(Constants.MAP_LON, "not available"),
            sharedPreferences.getString(Constants.MAP_LAT, "not available"))
    }

    fun setLatLonGPS(context: Context,long:String,lat:String){
        val  sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.GPS_LON, long)
        editor.putString(Constants.GPS_LAT, lat)
        editor.apply()
    }
    fun getLatLonGPS(context: Context): Pair<String?, String?> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return Pair(
            sharedPreferences.getString(Constants.GPS_LON, "not available"),
            sharedPreferences.getString(Constants.GPS_LAT, "not available"))
    }

    fun saveCityName(context: Context, cityName: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.COUNTRY_NAME, cityName)
        editor.apply()
    }
    fun getCityName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.COUNTRY_NAME, "Cairo") ?: DEFAULT_LOCATION
    }
    fun saveAlertType(context: Context, alertType: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.ALERT_TYPE, alertType)
        editor.apply()
    }
    fun getAlertType(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.ALERT_TYPE, Constants.Enum_ALERT.NOTIFICATION.toString()) ?: Constants.Enum_ALERT.NOTIFICATION.toString()
    }


    fun getSelectedSpeedUnit(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return when (sharedPreferences.getString(
            KEY_SELECTED_TEMPERATURE_UNIT,
            DEFAULT_TEMPERATURE_UNIT
        )) {
            "imperial" -> {
                context.getString(R.string.MilePerHour)
            }
            else -> {
                context.getString(R.string.MeterPerSecond)
            }
        }
    }
    fun getTemperatureUnitString(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return when (sharedPreferences.getString(
            KEY_SELECTED_TEMPERATURE_UNIT,
            DEFAULT_TEMPERATURE_UNIT
        )){
            "imperial" -> context.getString(R.string.f)
            "metric" -> context.getString(R.string.c)
            "default" -> context.getString(R.string.k)
            else -> context.getString(R.string.c)
        }
    }
}
