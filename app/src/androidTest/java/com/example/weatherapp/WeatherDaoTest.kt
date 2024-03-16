package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.DataBase.LocalDataSourceImpl
import com.example.weatherapp.DataBase.WeatherDataBase
import com.example.weatherapp.model.FavoriteCity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {
    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var database : WeatherDataBase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
    }
    @After
    fun closeDb() = database.close()
    @Test
    fun insertAndGetWeather() = runBlockingTest {
        // Given
        val favoriteCity = FavoriteCity("ismailia", 31.3301, 30.0566, "31.330130.0566")
        database.getWeatherDao().insertCity(favoriteCity)
        // When
        val loadedWeather = database.getWeatherDao().getAllFav().first()
        // Then
        assertThat(loadedWeather.size, `is`(1))
        assertThat(loadedWeather[0].cityName, `is`("ismailia"))
        assertThat(loadedWeather[0].latitude, `is`(31.3301))

    }

    @Test
    fun deleteWeather() = runBlockingTest {
        // Given
        val favoriteCity = FavoriteCity("ismailia", 31.3301, 30.0566, "31.330130.0566")
        database.getWeatherDao().insertCity(favoriteCity)

        // When
        val loadedWeather = database.getWeatherDao().getAllFav().first()
        assertThat(loadedWeather.size, `is`(1))

        database.getWeatherDao().deleteCity(favoriteCity)

        // Then
        val loaded = database.getWeatherDao().getAllFav().first()
        assertThat(loaded.isEmpty(), `is`(true))


    }
    @Test
    fun GetFavorite() = runBlockingTest {
        // Given
        val favoriteCity1 = FavoriteCity("ismailia", 31.3301, 30.0566, "31.330130.0566")
        val favoriteCity2 = FavoriteCity("cairo", 30.0444, 31.2357, "30.044431.2357")
        database.getWeatherDao().insertCity(favoriteCity1)
        database.getWeatherDao().insertCity(favoriteCity2)

        // When
        val favProduct = database.getWeatherDao().getAllFav()

        // Then
        val favProducts = favProduct.first()
        assertThat(favProducts.size, `is`(2))
    }
}
