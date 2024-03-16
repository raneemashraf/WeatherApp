package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapp.DataBase.LocalDataSourceImpl
import com.example.weatherapp.DataBase.WeatherDataBase
import com.example.weatherapp.model.FavoriteCity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var database : WeatherDataBase
    lateinit var localDataSource: LocalDataSourceImpl



    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),WeatherDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        localDataSource = LocalDataSourceImpl(ApplicationProvider.getApplicationContext())
    }
    @After
    fun closeDb(){
        database.close()
    }
    @Test
    fun insertProduct() = runBlockingTest {
        //Given
        val favoriteCity = FavoriteCity("ismailia", 31.3301, 30.0566, "31.330130.0566")

        //when
        localDataSource.insertCity(favoriteCity)

        //Then
        val result = localDataSource.getAllFavCity().first()
        MatcherAssert.assertThat(result[0].cityName, CoreMatchers.`is`(favoriteCity.cityName))

    }

    @Test
    fun deleteFavoriteCity() = runBlockingTest {
        // Given
        val favoriteCity = FavoriteCity("ismailia", 31.3301, 30.0566, "31.330130.0566")
        val favoriteCity2 = FavoriteCity("cairo", 31.31, 30.6, "31.3330.0566")
        val favoriteCity3 = FavoriteCity("Alex", 31.331, 30.056, "31.330.0566")

        localDataSource.insertCity(favoriteCity)
        localDataSource.insertCity(favoriteCity2)
        localDataSource.insertCity(favoriteCity3)


        //when
        localDataSource.deleteCity(favoriteCity)
        val result2 = localDataSource.getAllFavCity().first()

        //Then
        MatcherAssert.assertThat(result2.size, CoreMatchers.`is`(2))
    }


    @Test
    fun getAllFav() = runBlockingTest {
        // Given Prepare
        val favoriteCity1 = FavoriteCity("ismaili", 31.3301, 30.0566, "31.30130.0566")
        val favoriteCity2 = FavoriteCity("cairo", 30.0444, 31.2357, "30.04431.2357")

        // When call
        localDataSource.insertCity(favoriteCity1)
        localDataSource.insertCity(favoriteCity2)

        // Then Verify the expected outcome
        var result: List<FavoriteCity> = localDataSource.getAllFavCity().first()

        //MatcherAssert.assertThat(result.size, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(result, CoreMatchers.hasItem(favoriteCity1))
        MatcherAssert.assertThat(result, CoreMatchers.hasItem(favoriteCity2))

    }


}