package com.example.weatherapp.favorite.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.DataBase.LocalDataSourceImpl
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.databinding.FragmentFavoriteWeatherBinding
import com.example.weatherapp.home.view.HourWeatherAdapter
import com.example.weatherapp.home.view.WeekWeatherAdapter
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.model.repo.RepositoryImpl
import com.example.weatherapp.network.RemoteDataSourceImpl
import com.example.weatherapp.network.api.ApiState
import com.example.weatherapp.utils.PreferenceManager
import eg.iti.sv.weather.favweather.viewmodel.FavWeatherViewModel
import eg.iti.sv.weather.favweather.viewmodel.FavWeatherViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class FavoriteWeatherFragment : Fragment() {
    lateinit var binding: FragmentFavoriteWeatherBinding
    lateinit var viewModel : FavWeatherViewModel
    lateinit var viewModelFactory : FavWeatherViewModelFactory
    private lateinit var hourlyAdapter: HourWeatherAdapter
    private lateinit var hourLayoutManager: LinearLayoutManager
    private lateinit var weeklyLayoutManager: LinearLayoutManager
    private lateinit var weeklyAdapter: WeekWeatherAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteWeatherBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val place = arguments?.get("weather")

        val tempUnit: String = when (PreferenceManager.getSelectedTemperatureUnit(requireContext())) {
            "imperial" -> getString(R.string.f)
            "metric" -> getString(R.string.c)
            "default" -> getString(R.string.k)
            else -> getString(R.string.c) // Default to Celsius if the unit is not recognized
        }

        viewModelFactory = FavWeatherViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl(), LocalDataSourceImpl(requireContext())
            ))

        viewModel = ViewModelProvider(this, viewModelFactory).get(FavWeatherViewModel::class.java)
        viewModel.getWeatherOverNetwork(requireContext(),place as FavoriteCity)
        lifecycleScope.launch {
            viewModel.weather.collectLatest {
                when(it){
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.homaConstraint.visibility = View.GONE
                    }
                    is ApiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.homaConstraint.visibility = View.VISIBLE
                        binding.txtViewLocation.text = place.cityName
                        binding.txtViewTemperature.text = it.data.current.temp.toInt().toString() + tempUnit
                        binding.textViewCondition.text = it.data.current.weather[0].description
                        //binding.txtViewDate.text = getDateString(it.data.current.dt)
                        val sdf = SimpleDateFormat("dd MMM, yyyy, hh:mm")
                        val currentDate = sdf.format(Date())
                        binding.txtViewDate.text = currentDate
                        binding.pressureMeasure.text = it.data.current.pressure.toString()
                        binding.ultraVioMeasure.text = it.data.current.uvi.toString()
                        binding.cloudMeasure.text = it.data.current.clouds.toString()+"%"
                        binding.humidityMeasure.text = it.data.current.humidity.toString()+"%"
                        binding.windMeasure.text = it.data.current.wind_speed.toString()+"Km/h"
                        binding.visibilityMeasure.text = it.data.current.visibility.toString()+"Km"

                        Glide
                            .with(activity?.applicationContext as Context)
                            .load("https://openweathermap.org/img/wn/" + it.data.current.weather.get(0).icon + ".png")
                            .into(binding.imgWeather)

                        //hourly
                        hourLayoutManager = LinearLayoutManager(activity?.applicationContext as Context)
                        hourLayoutManager.orientation = RecyclerView.HORIZONTAL
                        hourlyAdapter = HourWeatherAdapter(
                            activity?.applicationContext as Context)
                        hourlyAdapter.submitList(it.data.hourly)

                        binding.recyclerViewForTime.adapter = hourlyAdapter
                        binding.recyclerViewForTime.layoutManager = hourLayoutManager


                        //Daily
                        weeklyLayoutManager  = LinearLayoutManager(activity?.applicationContext as Context)
                        weeklyLayoutManager.orientation = RecyclerView.VERTICAL
                        weeklyAdapter = WeekWeatherAdapter(activity?.applicationContext as Context,
                            it.data.daily)
                        binding.recyclerViewForday.adapter = weeklyAdapter
                        binding.recyclerViewForday.layoutManager = weeklyLayoutManager
                        Log.i("TAG", "getCityName: ${it.data.lon }${it.data.lat}")
                        Log.i("TAG", "onViewCreated: ${it.data.current}")
                    }

                    is ApiState.Failure  ->  {
                        Log.i("TAG", "faild: ")
                    }
                }
            }

        }
    }
}