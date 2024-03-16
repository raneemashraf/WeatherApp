package com.example.weatherapp.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
@Dao
interface WeatherDao {
    @Query("SELECT * FROM FavoriteCities")
    fun getAllFav(): Flow<List<FavoriteCity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(favoriteCity: FavoriteCity)
    @Delete
    fun deleteCity(favoriteCity: FavoriteCity)


    @Insert(entity = AlertDto::class)
    fun insertIntoAlert(alertEntity: AlertDto)
    @Delete(entity = AlertDto::class)
    fun removeFromAlerts(alertEntity: AlertDto)
    @Query("select * from AlertTable")
    fun getAlerts(): Flow<List<AlertDto>>



    @Query("Select * from Weather")
    fun getCurrentWeather(): Flow<List<WeatherResponse>>
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = WeatherResponse::class)
    fun insertWeather(weather: WeatherResponse)
    @Query("Delete from Weather")
    suspend fun deleteCurrentWeather()

}