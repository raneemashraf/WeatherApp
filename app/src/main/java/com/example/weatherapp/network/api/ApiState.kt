package com.example.weatherapp.network.api

import com.example.weatherapp.model.WeatherResponse
import java.io.Serializable

sealed class ApiState<out T> {
    class Success<T>(val data:T): ApiState<T>()
    class Failure(val msg:Throwable): ApiState<Nothing>()
    object Loading: ApiState<Nothing>()
}