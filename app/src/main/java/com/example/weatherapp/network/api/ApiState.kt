package com.example.weatherapp.network.api

import com.example.weatherapp.model.WeatherResponse
import java.io.Serializable

sealed class ApiState : Serializable {
    class Success (val data: WeatherResponse): ApiState()
    class Failure (val msg: Throwable): ApiState()
    object Loading: ApiState()
}