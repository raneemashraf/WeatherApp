package com.example.weatherapp.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.home.view.CurrentLocation.Companion.locationStateFlow
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.network.api.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeViewModel(private val _repo: Repository) : ViewModel() {

    private var _weather = MutableStateFlow<ApiState>(ApiState.Loading)
    val weather = _weather.asStateFlow()

    fun getWeatherOverNetwork(context: Context)
    = viewModelScope.launch(Dispatchers.IO) {
        locationStateFlow.collectLatest {
            val lon = it
            val locList = lon.split(",")
            _repo.getWeather(lon = locList[0], lat = locList[1])
                .catch { e ->
                    _weather.value = ApiState.Failure(e)

                }.collect {
                    _weather.value = ApiState.Success(it)
                }
        }
    }

//    fun getWeatherOverNetwork(context: Context) {
//        locationStateFlow.collectLatest { location ->
//            val lonLatAddress = location.split(",")
//            val lon = lonLatAddress[0]
//            val lat = lonLatAddress[1]
//            viewModelScope.launch {
//                try {
//                    val weatherData = _repo.getWeather(lon, lat)
//                    _weather.value = ApiState.Success(weatherData)
//                } catch (e: Exception) {
//                    _weather.value = ApiState.Failure(e)
//                }
//            }
//        }
//    }
//}
}