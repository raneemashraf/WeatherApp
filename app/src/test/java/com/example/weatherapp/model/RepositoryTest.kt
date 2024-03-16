package com.example.weatherapp.model

import com.example.weatherapp.network.FakeRemoteSource
import com.example.weatherapp.MainRule
import com.example.weatherapp.db.FakeLocalSource
import com.example.weatherapp.model.repo.RepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepositoryTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    val localSourcePlacesList = mutableListOf(
        FavoriteCity("place1", 0.0, 0.0, "00"),
        FavoriteCity("place2", 1.0, 1.0, "11")
    )

    val remoteSourceResponse = WeatherResponse(
        listOf(),
        Current(
            0, 0.0, 0, 0.0, 0, 0, 0, 0, 0.0, 0.0, 0, listOf<Weather>(), 0, 0.0
        ), listOf(), listOf(), 0.0, 0.0, "", 0
    )

    lateinit var fakeLocalDataSource: FakeLocalSource
    lateinit var fakeRemoteDataSource: FakeRemoteSource
    lateinit var repository: RepositoryImpl
    lateinit var result: WeatherResponse


    @Before
    fun setUp() {
        fakeLocalDataSource = FakeLocalSource(localSourcePlacesList)
        fakeRemoteDataSource = FakeRemoteSource(remoteSourceResponse)
        repository = RepositoryImpl.getInstance(fakeRemoteDataSource, fakeLocalDataSource) as RepositoryImpl
    }

    @Test
    fun getWeatherOverNetwork_lonLat_weatherResponse() = mainRule.runBlockingTest {

        repository.getWeather("", "","","")
            .collect {
            result = it
        }
        Assert.assertThat(result, CoreMatchers.`is`(remoteSourceResponse))
    }

    @Test
    fun removePlaceFromFav_place_dbUpdated() = mainRule.runBlockingTest {

        //Given
        var result = listOf<FavoriteCity>()
        val place = FavoriteCity("mansoura", 1.0, 1.0, "11")
        repository.insertCity(place)

        //When
        repository.deleteCity(place)
        repository.getAllFavCity().collect {
            result = it
        }
        //Return
       // Assert.assertThat(result.size, CoreMatchers.`is`(2))
        Assert.assertThat(result[(result.size)-1].cityName, CoreMatchers.`is`(not(place.cityName)))



    }



    @Test
    fun addPlaceToFav_place_dbUpdated() = mainRule.runBlockingTest {
        //Given
        val place = FavoriteCity("place3", 3.0, 3.0, "33")

        //When
        repository.insertCity(place)

        var result = listOf<FavoriteCity>()
        repository.getAllFavCity().collect {
            result = it
        }

        //Return
        Assert.assertThat(result[2], CoreMatchers.`is`(place))

    }

    @Test
    fun getAllFavPlaces_returnFavPlaces() = mainRule.runBlockingTest {
        //when
        var result = listOf<FavoriteCity>()
        repository.getAllFavCity().collect {
            result = it
        }
        //Return
        Assert.assertThat(result.isEmpty(), CoreMatchers.`is`(false))

        //Assert.assertThat(result, CoreMatchers.`is`(localSourcePlacesList))

    }
}