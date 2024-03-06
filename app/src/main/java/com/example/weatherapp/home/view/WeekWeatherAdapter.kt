package com.example.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.RowDayBinding
import com.example.weatherapp.model.Daily
import com.example.weatherapp.utils.getDateString
class WeekWeatherAdapter (private val context: Context, private var daily: List<Daily>): RecyclerView.Adapter<WeekWeatherAdapter.ViewHolder>(){
    private lateinit var binding: RowDayBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RowDayBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun setList(myDaily: List<Daily>) {
        daily = myDaily
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = daily.get(position)
//
//        val lon = it
//        val locList = lon.split(",")
//        _repo.getWeather(lon = locList[0], lat = locList[1])
        binding.txtDayName.text = getDateString(currentItem.dt)
        binding.txtMaxTempDay.text = currentItem.temp.min.toString() +" / " + currentItem.temp.max.toString()
        binding.txtDescDay.text = currentItem.weather.get(0).description
        Glide
            .with(context)
            .load("https://openweathermap.org/img/wn/"+currentItem.weather.get(0).icon+".png")
            .into(binding.iconDay)
    }

    override fun getItemCount(): Int = daily.size

    inner class ViewHolder(binding: RowDayBinding) : RecyclerView.ViewHolder(binding.root)

}
