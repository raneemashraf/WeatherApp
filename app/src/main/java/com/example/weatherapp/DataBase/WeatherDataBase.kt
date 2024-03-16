package com.example.weatherapp.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.AlertDto
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherResponse

@Database(entities = [FavoriteCity::class,AlertDto::class, WeatherResponse::class], version = 1)
@TypeConverters(WeatherModelTypeConverter::class)

abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao() : WeatherDao
    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance(ctx: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBase::class.java, "weather_table"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

//@Database(entities = arrayOf(ProductsItem::class), version = 1 )
//abstract class MyDataBase: RoomDatabase(){
//    abstract fun getProductDao(): ProductsDao
//
//    companion object{ // b7ot el7agat elly kont b3mlha static zy el get instance
//        @Volatile
//        private var INSTANCE : MyDataBase? = null
//        fun getInstance(ctx: Context):MyDataBase{
//            return INSTANCE ?: synchronized(this){
//                val instance = Room.databaseBuilder(
//                    ctx.applicationContext,MyDataBase::class.java,"products_table"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}