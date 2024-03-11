package com.example.weatherapp.network

import com.example.weatherapp.model.WeatherResponse

interface RemoteDataSource {
    suspend fun getWeather(
        lat: String?,
        lon: String?,
        lang: String?,
        units: String?
    ): WeatherResponse
}
