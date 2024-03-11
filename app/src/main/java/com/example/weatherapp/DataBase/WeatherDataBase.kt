package com.example.weatherapp.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.model.FavoriteCity

@Database(entities = [FavoriteCity::class], version = 1)
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