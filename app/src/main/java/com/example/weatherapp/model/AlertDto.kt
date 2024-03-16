package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlertTable")
data class AlertDto(
    @PrimaryKey(autoGenerate = true)
    var alertID:Int = 0,
    val startDate: Long,
    val endDate: Long ,
    val time: Long,
    val placeName: String ,

)