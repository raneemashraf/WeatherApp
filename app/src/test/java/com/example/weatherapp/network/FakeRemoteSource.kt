package com.example.weatherapp.network

import com.example.weatherapp.model.WeatherResponse


class FakeRemoteSource(private var weatherResponse: WeatherResponse):RemoteDataSource {
    override suspend fun getWeather(
        lat: String?,
        lon: String?,
        lang: String?,
        units: String?
    ): WeatherResponse {
        return weatherResponse
    }
}