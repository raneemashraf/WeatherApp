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
import com.example.weatherapp.utils.PreferenceManager.getAppLocationByMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeViewModel(private val _repo: Repository) : ViewModel() {

    private var _weather = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Loading)
    val weather = _weather.asStateFlow()

    var _currentWeather= MutableStateFlow<ApiState<List<WeatherResponse>>>(ApiState.Loading)


    fun getWeatherOverNetwork(context: Context)
    = viewModelScope.launch(Dispatchers.IO) {
        if (PreferenceManager.getSelectedLocation(context) == Constants.LOCATION_MAP) {
            val pair = getAppLocationByMap(context)
            _repo.getWeather(pair.first, pair.second,
                lang = PreferenceManager.getSelectedLanguage(context),
                units = PreferenceManager.getSelectedTemperatureUnit(context))
                .catch { e ->
                    _weather.value = ApiState.Failure(e)
                }.collect {
                    _weather.value = ApiState.Success(it)
                    _repo.insertWeather(it)
                }
        }else {
            locationStateFlow.collectLatest {
                Log.i("TAG", "getWeatherOverNetwork: " + it)
                val lon = it
                val locList = lon.split(",")
                _repo.getWeather(
                    lon = locList[0], lat = locList[1],
                    lang = PreferenceManager.getSelectedLanguage(context),
                    units = PreferenceManager.getSelectedTemperatureUnit(context)
                )
                    .catch { e ->
                        _weather.value = ApiState.Failure(e)
                    }.collect {
                        _weather.value = ApiState.Success(it)
                        _repo.insertWeather(it)
                    }
            }
        }
    }
    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getCurrentWeather()
                ?.catch { e-> _currentWeather.value = ApiState.Failure(e) }
                ?.collect{
                    _currentWeather.value = ApiState.Success(it)
                }
        }
    }

    fun insertCurrentWeather(weather: WeatherResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertWeather(weather)
        }
    }
    fun deleteCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteCurrentWeather()
        }
    }
}