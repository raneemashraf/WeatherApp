package com.example.weatherapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.home.view.CurrentLocation
import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.network.api.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertViewModel(private val _repo:Repository): ViewModel() {
    private var alertResponse = MutableStateFlow<ApiState<List<AlertDto>>>(ApiState.Loading)
    val onlineAlerts = alertResponse.asStateFlow()

    private var weatherResponse = MutableStateFlow<ApiState<List<WeatherResponse>>>(ApiState.Loading)

    init {
        getAllAlerts()
    }
    fun getAllAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getAlerts()
                .catch { e-> alertResponse.value= ApiState.Failure(e) }
                .collect{ data -> alertResponse.value= ApiState.Success(data) }
        }
    }

    fun removeAlert(alert: AlertDto) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.removeFromAlerts(alert)
        }
    }

    fun addAlert(alert: AlertDto){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertIntoAlert(alert)
        }
    }


}