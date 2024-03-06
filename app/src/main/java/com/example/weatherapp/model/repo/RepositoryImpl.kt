package com.example.weatherapp.model.repo

import android.util.Log
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RepositoryImpl private constructor(var remoteDataSource: RemoteDataSource)
    :Repository {
    companion object{
        private var instance:RepositoryImpl? = null
        fun getInstance(
            remoteSource: RemoteDataSource,
           // localSource: LocalSource
        ):Repository {
            return instance ?: synchronized(this){
                val temp = RepositoryImpl(remoteSource)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getWeather(
        lat: String?,
        lon: String?,
//        lang: String?,
  //      units: String?
    ): Flow<WeatherResponse> {
        return flowOf(remoteDataSource.getWeather(lat, lon))
        //Log.i("TAG", "onCreateView: " + remoteDataSource.getWeather("33.44", "-94.04").city)

    }
}