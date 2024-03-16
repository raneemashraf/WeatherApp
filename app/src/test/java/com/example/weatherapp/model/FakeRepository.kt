package com.example.weatherapp.model

import com.example.weatherapp.db.FakeLocalSource
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.network.FakeRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository(
    private var fakeRemoteSource: FakeRemoteSource,
    private var fakeLocalSource: FakeLocalSource
): Repository {
    var places:MutableList<FavoriteCity>? = mutableListOf()
    override suspend fun getWeather(
        lat: String?,
        lon: String?,
        lang: String?,
        units: String?
    ): Flow<WeatherResponse> {
        return flowOf( fakeRemoteSource.getWeather(lat,lon,lang,units))
    }

    override suspend fun getAllFavCity(): Flow<List<FavoriteCity>> {
       //return fakeLocalSource.getAllFavCity()
        return flowOf(places as List<FavoriteCity>)
    }

    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        //fakeLocalSource.insertCity(favoriteCity)
        places?.add(favoriteCity)

    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        //fakeLocalSource.deleteCity(favoriteCity)
        places?.remove(favoriteCity)
    }

    override suspend fun getAlerts(): Flow<List<AlertDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertIntoAlert(alertDto: AlertDto) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromAlerts(alertDto: AlertDto) {
        TODO("Not yet implemented")
    }
}