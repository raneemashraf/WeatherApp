package eg.iti.sv.weather.favweather.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.network.api.ApiState
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavWeatherViewModel(private val _repo: Repository) :ViewModel(){

    private var _weather = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Loading)
    val weather  = _weather.asStateFlow()

    fun getWeatherOverNetwork(context: Context, place: FavoriteCity) = viewModelScope.launch(Dispatchers.IO) {
        _repo.getWeather(lon = place.longitude.toString(), lat = place.latitude.toString(),lang = PreferenceManager.getSelectedLanguage(context), units = PreferenceManager.getSelectedTemperatureUnit(context))
            .catch {e->
                _weather.value = ApiState.Failure(e)

            }.collect{
                _weather.value = ApiState.Success(it)
            }
    }
}