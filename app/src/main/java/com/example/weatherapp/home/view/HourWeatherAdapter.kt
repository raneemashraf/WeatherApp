package com.example.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.RowHourlyBinding
import com.example.weatherapp.model.Hourly
import com.example.weatherapp.utils.PreferenceManager
import com.example.weatherapp.utils.getHourString

class HourWeatherAdapter(
    private val context: Context
) : ListAdapter<Hourly, HourWeatherAdapter.ViewHolder>(HourlyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHourlyBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: RowHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hourly: Hourly) {
            binding.txtTime.text = getHourString(hourly.dt)
            binding.txtTempTime.text = hourly.temp.toInt().toString()+PreferenceManager.getTemperatureUnitString(context)
            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${hourly.weather[0].icon}.png")
                .into(binding.iconTime)
        }
    }

    class HourlyDiffCallback : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem == newItem
        }
    }
}
