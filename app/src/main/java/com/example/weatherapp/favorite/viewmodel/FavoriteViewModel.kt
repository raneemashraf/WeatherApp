package com.example.weatherapp.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.network.api.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel (private var _repository: Repository): ViewModel() {
    private val _favoriteList = MutableStateFlow<ApiState<List<FavoriteCity>>>(ApiState.Loading)
    val onlineFavorite = _favoriteList.asStateFlow()

    init {
        getAllFavCity()
    }

    fun getAllFavCity() = viewModelScope.launch(Dispatchers.IO) {
        _repository.getAllFavCity()
            .catch {e->
                _favoriteList.value = ApiState.Failure(e)
            }.collect{
                _favoriteList.value = ApiState.Success(it)
            }
    }

    fun deleteFavCity(city: FavoriteCity){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.deleteCity(city)
        }
    }


}