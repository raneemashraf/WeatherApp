package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "FavoriteCities")
data class FavoriteCity (
    val cityName : String,
    val latitude:Double,
    val longitude:Double,
    @PrimaryKey
    val latLog:String
):Serializable
