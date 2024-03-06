package com.example.weatherapp.model.repo

import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getWeather(
        lat: String?,
        lon: String?,
//        lang: String?,
//        units: String?
    ):Flow<WeatherResponse>
}