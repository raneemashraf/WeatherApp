package com.example.weatherapp.DataBase

import android.content.Context
import com.example.weatherapp.model.FavoriteCity
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(context: Context):LocalDataSource {
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
}