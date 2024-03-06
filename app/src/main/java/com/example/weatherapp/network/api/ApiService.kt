package com.example.weatherapp.network.api

import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("exclude") exclude: String? = "minutely",
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String? = "en",
        @Query("appid") appid: String?="40dac0af7018969cbb541943f944ba29"
    ): Response<WeatherResponse>
}
//"40dac0af7018969cbb541943f944ba29" onecall
//e3dcf60855b274191580532e7e5968db forcast
