package com.example.weatherapp.network.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private var retrofit: Retrofit? = null
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private fun getInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
    fun getApi(): ApiService {
        return getInstance().create(ApiService::class.java)
    }
}
