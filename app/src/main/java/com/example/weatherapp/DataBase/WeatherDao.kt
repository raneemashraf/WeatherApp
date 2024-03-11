package com.example.weatherapp.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.FavoriteCity
import kotlinx.coroutines.flow.Flow
@Dao
interface WeatherDao {
    @Query("SELECT * FROM FavoriteCities")
    fun getAllFav(): Flow<List<FavoriteCity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(favoriteCity: FavoriteCity)

    @Delete
    fun deleteCity(favoriteCity: FavoriteCity)

}