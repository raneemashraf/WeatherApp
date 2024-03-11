package com.example.weatherapp.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.repo.Repository


class MapViewModelFactory(private val _repo: Repository)
    : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(MapViewModel::class.java)){
            MapViewModel(_repo) as T
        }else
            throw IllegalArgumentException("class not found")
    }
}