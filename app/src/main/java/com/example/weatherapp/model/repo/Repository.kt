package com.example.weatherapp.model.repo

import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getWeather(
        lat: String?,
        lon: String?,
        lang: String?,
        units: String?
    ):Flow<WeatherResponse>
    suspend fun getAllFavCity(): Flow<List<FavoriteCity>>
    suspend fun insertCity(favoriteCity: FavoriteCity)
    suspend fun deleteCity(favoriteCity: FavoriteCity)

    suspend fun getAlerts(): Flow<List<AlertDto>>
    suspend fun insertIntoAlert(alertDto: AlertDto)
    suspend fun removeFromAlerts(alertDto: AlertDto)

    fun getCurrentWeather(): Flow<List<WeatherResponse>>
    fun insertWeather(weather: WeatherResponse)
    suspend fun deleteCurrentWeather()
}