package com.example.weatherapp.DataBase

import android.content.Context
import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(context: Context):LocalDataSource{

    private val weatherDao: WeatherDao =
        WeatherDataBase.getInstance(context).getWeatherDao()

    override suspend fun getAllFavCity(): Flow<List<FavoriteCity>> {
       return weatherDao.getAllFav()
    }
    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        weatherDao.insertCity(favoriteCity)
    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
       weatherDao.deleteCity(favoriteCity)
    }

    override suspend fun insertIntoAlert(alertDto: AlertDto) {
        weatherDao.insertIntoAlert(alertDto)
    }

    override suspend fun removeFromAlerts(alertDto: AlertDto) {
        weatherDao.removeFromAlerts(alertDto)
    }

    override suspend fun getAlerts(): Flow<List<AlertDto>> {
        return weatherDao.getAlerts()
    }

    override fun getCurrentWeather(): Flow<List<WeatherResponse>> {
        return weatherDao.getCurrentWeather()
    }

    override fun insertWeather(weather: WeatherResponse) {
        weatherDao.insertWeather(weather)
    }

    override suspend fun deleteCurrentWeather() {
        weatherDao.deleteCurrentWeather()
    }
}