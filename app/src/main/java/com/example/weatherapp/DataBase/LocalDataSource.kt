package com.example.weatherapp.DataBase

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
   suspend fun getAllFavCity(): Flow<List<FavoriteCity>>
   suspend fun insertCity(favoriteCity: FavoriteCity)
   suspend fun deleteCity(favoriteCity: FavoriteCity)


   suspend fun insertIntoAlert(alertDto: AlertDto)
   suspend fun removeFromAlerts(alertDto: AlertDto)
   suspend fun getAlerts(): Flow<List<AlertDto>>


   fun getCurrentWeather(): Flow<List<WeatherResponse>>
   fun insertWeather(weather: WeatherResponse)
   suspend fun deleteCurrentWeather()
}