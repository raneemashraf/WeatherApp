package com.example.weatherapp.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.DataBase.LocalDataSourceImpl
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.model.repo.RepositoryImpl
import com.example.weatherapp.network.RemoteDataSource
import com.example.weatherapp.network.RemoteDataSourceImpl
import com.example.weatherapp.network.api.ApiState
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.PreferenceManager
import com.example.weatherapp.utils.getDateString
import com.example.weatherapp.utils.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private lateinit var geocoder: Geocoder
    private lateinit var currentLocation: CurrentLocation
    private lateinit var hourlyAdapter: HourWeatherAdapter
    private lateinit var hourLayoutManager: LinearLayoutManager
    private lateinit var weeklyLayoutManager: LinearLayoutManager
    private lateinit var weeklyAdapter: WeekWeatherAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }
    override fun onResume() {
        super.onResume()
        currentLocation.getLastLocation()
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geocoder = Geocoder(requireContext())
        currentLocation = CurrentLocation(requireActivity(),requireContext())
        currentLocation.getLastLocation()

        viewModelFactory = HomeViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl(),LocalDataSourceImpl(requireContext())))

            viewModel = ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)
            //viewModel.getWeatherOverNetwork(requireContext())



        if (isNetworkAvailable(requireContext())) {
            viewModel.getWeatherOverNetwork(requireContext())
        } else {
            Snackbar.make(binding.root,
                getString(R.string.checkInternet),
                Snackbar.LENGTH_LONG).show()
            viewModel.getCurrentWeather()
        }

        lifecycleScope.launch {
                viewModel.weather.collectLatest {
                    when(it){
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.parent.visibility = View.GONE
                            binding.constraintLayout.visibility = View.GONE
                        }
                        is ApiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.parent.visibility = View.VISIBLE
                            binding.constraintLayout.visibility = View.VISIBLE

                            val cityName = getCityName(it.data.lon,it.data.lat)
                            binding.txtViewLocation.text = cityName
                            PreferenceManager.saveCityName(requireContext(),cityName)

                            binding.txtViewTemperature.text = it.data.current.temp.toInt().toString() +" "+ PreferenceManager.getTemperatureUnitString(requireContext())
                            binding.textViewCondition.text = it.data.current.weather[0].description
                            //binding.txtViewDate.text = getDateString(it.data.current.dt)
                            val sdf = SimpleDateFormat("dd MMM, yyyy, hh:mm")
                            val currentDate = sdf.format(Date())
                            binding.txtViewDate.text = currentDate
                            binding.pressureMeasure.text = it.data.current.pressure.toString()
                            binding.ultraVioMeasure.text = it.data.current.uvi.toString()
                            binding.cloudMeasure.text = it.data.current.clouds.toString() + "%"
                            binding.humidityMeasure.text = it.data.current.humidity.toString()+"%"
                            binding.windMeasure.text = "${it.data.current.wind_speed} ${PreferenceManager.getSelectedSpeedUnit(requireContext())}"

                            binding.visibilityMeasure.text = it.data.current.visibility.toString()+"Km"

                            Glide
                                .with(activity?.applicationContext as Context)
                                .load("https://openweathermap.org/img/wn/" + it.data.current.weather.get(0).icon + ".png")
                                .into(binding.imgWeather)

                            //hourly
                            hourLayoutManager = LinearLayoutManager(activity?.applicationContext as Context)
                            hourLayoutManager.orientation = RecyclerView.HORIZONTAL
                            hourlyAdapter = HourWeatherAdapter(activity?.applicationContext as Context,)
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
                             binding.progressBar.visibility = View.VISIBLE
                             binding.parent.visibility = View.GONE

                         }
                    }
                }
            }

    }


    suspend fun getCityName(longitude: Double, altitude: Double): String {
        return withContext(Dispatchers.IO) {
            try {
                val theAddress = geocoder.getFromLocation(altitude, longitude, 5)
                if (theAddress!!.isNotEmpty()) {
                    return@withContext theAddress[0]?.adminArea ?: ""
                } else {
                    return@withContext ""
                }
            } catch (e: Exception) {
                Log.e("HomeFragmentError", "Error getting city name: ${e.message}")
                return@withContext ""
            }
        }
    }


}
