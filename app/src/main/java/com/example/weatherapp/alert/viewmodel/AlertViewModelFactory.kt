package com.example.weatherapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.repo.Repository

class AlertViewModelFactory (private val _iRepo: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java))
        {
            AlertViewModel(_iRepo) as T
        }else{
            throw IllegalAccessException("View Model Class not found")
        }
    }
}