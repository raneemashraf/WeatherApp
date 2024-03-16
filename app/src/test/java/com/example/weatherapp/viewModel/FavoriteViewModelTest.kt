package com.example.weatherapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainRule
import com.example.weatherapp.db.FakeLocalSource
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapp.map.viewmodel.MapViewModel
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.FakeRepository
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.FakeRemoteSource
import com.example.weatherapp.network.api.ApiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FavoriteViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule

    val mainRule = MainRule()

    val fav1 = FavoriteCity("Cairo", 48.4734, 7.9498,"7.9498" )
    val fav2 = FavoriteCity("ismailia", 48.4734, 7.9498,"7.949" )
    val fav3 = FavoriteCity("Alex", 48.4734, 7.9498,"7.94" )


    val weather1 = WeatherResponse(
        alerts = emptyList(),
        current =Current(
            clouds = 0,
            dew_point = 0.0,
            dt = 0,
            feels_like = 0.0,
            humidity = 0,
            pressure = 0,
            sunrise = 0,
            sunset = 0,
            temp = 0.0,
            uvi = 0.0,
            visibility = 0,
            weather = emptyList(),
            wind_deg = 0,
            wind_speed = 0.0
        ),
        daily = emptyList(),
        hourly = emptyList(),
        lat = 22.2,
        lon = 555.2,
        timezone = "cairo",
        timezone_offset = 555
    )

    private val local = listOf(fav1, fav2, fav3)
    private val remote = weather1

    lateinit var repo: FakeRepository
    lateinit var mapViewModel: MapViewModel
    lateinit var favViewModel: FavoriteViewModel
    private lateinit var remoteDataSource: FakeRemoteSource
    private lateinit var localDataSource: FakeLocalSource

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteSource(remote)
        localDataSource = FakeLocalSource(local as MutableList<FavoriteCity>)
        repo = FakeRepository(remoteDataSource,localDataSource)
        mapViewModel= MapViewModel(repo)
        favViewModel= FavoriteViewModel(repo)
    }

    @Test
    fun getFavWeather_insertWeather_returnWeatherName() = runBlockingTest {
        mapViewModel.insertCityToFav(fav1)
        favViewModel.getAllFavCity()
        var data:List<FavoriteCity> = emptyList()
        val result = favViewModel.onlineFavorite.first()

        when (result) {
            is ApiState.Success -> {
                data = result.data
            }
            else -> {}
        }
        //Then
        MatcherAssert.assertThat(data.get(0).cityName, CoreMatchers.`is`("Cairo"))
    }

    @Test
    fun deleteFavWeather_returnZero() = runBlockingTest{
        //Given
        mapViewModel.insertCityToFav(fav1)
        mapViewModel.insertCityToFav(fav2)

        //When
        favViewModel.deleteFavCity(fav1)
        delay(1000)
        favViewModel.getAllFavCity()
        //then
        var data:List<FavoriteCity> = emptyList()
        val result = favViewModel.onlineFavorite.first()

        when (result) {
            is ApiState.Success -> {
                data = result.data
            }
            else -> {}
        }
        //Then
        MatcherAssert.assertThat(data.size, CoreMatchers.`is`(1))


    }
}