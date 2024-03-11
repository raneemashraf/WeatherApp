package com.example.weatherapp.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.repo.Repository

class FavoriteViewModelFactory (private val _irpo: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java))
        {
            FavoriteViewModel(_irpo) as T
        }else{
            throw IllegalAccessException("View Model Class not found")
        }
    }
}