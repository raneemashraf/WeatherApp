package com.example.weatherapp.home.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.home.view.CurrentLocation.Companion.locationStateFlow
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.network.api.ApiState
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeViewModel(private val _repo: Repository) : ViewModel() {

    private var _weather = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Loading)
    val weather = _weather.asStateFlow()

    fun getWeatherOverNetwork(context: Context)
    = viewModelScope.launch(Dispatchers.IO) {
        locationStateFlow.collectLatest {
            Log.i("TAG", "getWeatherOverNetwork: "+it)
            val lon = it
            val locList = lon.split(",")
            _repo.getWeather(lon = locList[0], lat = locList[1], lang = PreferenceManager.getSelectedLanguage(context), units = PreferenceManager.getSelectedTemperatureUnit(context))
                .catch { e ->
                    _weather.value = ApiState.Failure(e)

                }.collect {
                    _weather.value = ApiState.Success(it)
                }
        }
    }
}