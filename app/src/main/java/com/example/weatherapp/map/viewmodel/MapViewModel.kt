package com.example.weatherapp.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private var _repo:Repository): ViewModel() {
    fun insertCityToFav(favoriteCity: FavoriteCity){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertCity(favoriteCity)
            Log.i("TAG", "insertCityToFav: ${favoriteCity.cityName}")
        }
    }
}