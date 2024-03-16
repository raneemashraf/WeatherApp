package com.example.weatherapp.db

import com.example.weatherapp.DataBase.LocalDataSource
import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.FavoriteCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class FakeLocalSource(
    private var favList: MutableList<FavoriteCity> = mutableListOf()
):LocalDataSource {

    override suspend fun getAllFavCity(): Flow<List<FavoriteCity>> {
        return flowOf(favList)
    }

    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        favList.add(favoriteCity)
    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        favList.remove(favoriteCity)
    }

    override suspend fun insertIntoAlert(alertDto: AlertDto) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromAlerts(alertDto: AlertDto) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlerts(): Flow<List<AlertDto>> {
        TODO("Not yet implemented")
    }
}