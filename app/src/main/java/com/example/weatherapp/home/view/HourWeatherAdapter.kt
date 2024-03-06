package com.example.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.RowHourlyBinding
import com.example.weatherapp.model.Hourly
import com.example.weatherapp.utils.getHourString

class HourWeatherAdapter(private val context: Context, private var hourly: List<Hourly>)
    : RecyclerView.Adapter<HourWeatherAdapter.ViewHolder>(){
    private lateinit var binding: RowHourlyBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RowHourlyBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun setList(myhourly: List<Hourly>) {
        hourly = myhourly
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = hourly.get(position)
        binding.txtTime.text = getHourString(currentItem.dt)
        binding.txtTempTime.text = currentItem.temp.toString()
        Glide
            .with(context)
            .load("https://openweathermap.org/img/wn/"+currentItem.weather.get(0).icon+".png")
            .into(binding.iconTime)

    }

    override fun getItemCount(): Int = hourly.size

    inner class ViewHolder(binding: RowHourlyBinding) : RecyclerView.ViewHolder(binding.root) {}

}
