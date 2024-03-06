package com.example.weatherapp.network

import com.example.weatherapp.network.api.RetrofitHelper
import com.example.weatherapp.model.WeatherResponse

class RemoteDataSourceImpl : RemoteDataSource {
    private val apiService = RetrofitHelper.getApi()
    override suspend fun getWeather(
        lat: String?,
        lon: String?,
//        lang: String?,
//        units: String?
    ): WeatherResponse {
        val root = apiService.getWeather(
            lat,lon)
        println(root.body())
        return root.body()!!

    }

}