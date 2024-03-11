package com.example.weatherapp.DataBase

import com.example.weatherapp.model.FavoriteCity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
   suspend fun getAllFavCity(): Flow<List<FavoriteCity>>
   suspend fun insertCity(favoriteCity: FavoriteCity)
   suspend fun deleteCity(favoriteCity: FavoriteCity)
}