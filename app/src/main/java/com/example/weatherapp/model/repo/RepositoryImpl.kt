package com.example.weatherapp.model.repo

import android.util.Log
import com.example.weatherapp.DataBase.LocalDataSource
import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RepositoryImpl private constructor(
    var remoteDataSource: RemoteDataSource,
    var localDataSource: LocalDataSource
) :Repository {
    companion object{
        private var instance:RepositoryImpl? = null
        fun getInstance(
            remoteSource: RemoteDataSource,
            localDataSource: LocalDataSource
           // localSource: LocalSource
        ):Repository {
            return instance ?: synchronized(this){
                val temp = RepositoryImpl(remoteSource,localDataSource)
                instance = temp
                temp
            }
        }
    }
    override suspend fun getWeather(
        lat: String?,
        lon: String?,
        lang: String?,
        units: String?
    ): Flow<WeatherResponse> {
        return flowOf(remoteDataSource.getWeather(lat, lon,lang,units))
    }

    override suspend fun getAllFavCity(): Flow<List<FavoriteCity>> {
       return localDataSource.getAllFavCity()
    }
    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        localDataSource.insertCity(favoriteCity)
    }
    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        localDataSource.deleteCity(favoriteCity)
    }

    override suspend fun getAlerts(): Flow<List<AlertDto>> {
        return localDataSource.getAlerts()
    }

    override suspend fun insertIntoAlert(alertDto: AlertDto) {
        localDataSource.insertIntoAlert(alertDto)
    }

    override suspend fun removeFromAlerts(alertDto: AlertDto) {
        localDataSource.removeFromAlerts(alertDto)
    }

    override fun getCurrentWeather(): Flow<List<WeatherResponse>> {
       return localDataSource.getCurrentWeather()
    }

    override fun insertWeather(weather: WeatherResponse) {
        localDataSource.insertWeather(weather)
        Log.i("TAG", "insertWeather: "+weather.current.clouds)
    }

    override suspend fun deleteCurrentWeather() {
        localDataSource.deleteCurrentWeather()
    }
}