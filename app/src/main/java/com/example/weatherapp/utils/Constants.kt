package com.example.weatherapp.utils

object Constants{
    const val APIKEY = "e3dcf60855b274191580532e7e5968db"
    const val BASE_URL = "https://api.openweathermap.org/data/3.0/"
    const val LOCATION_MAP = "map"
    const val LOCATION_GPS = "gps"
    const val LANGUAGE_AR = "ar"
    const val LANGUAGE_EN = "en"
    const val UNITS_CELSIUS = "metric"
    const val UNITS_kelvin = "default"
    const val UNITS_Fahrenheit = "imperial"
    const val MAP_LON="MAP_LON"
    const val MAP_LAT="MAP_LAT"
    const val GPS_LON = "GPS_LON"
    const val GPS_LAT = "GPS_LAT"
    const val COUNTRY_NAME = "COUNTRY_NAME"
    enum class Enum_ALERT(){ALARM,NOTIFICATION}
    const val ALERT_TYPE="ALERT_TYPE"


    //default: kelvin, metric: Celsius, imperial: Fahrenheit.
}