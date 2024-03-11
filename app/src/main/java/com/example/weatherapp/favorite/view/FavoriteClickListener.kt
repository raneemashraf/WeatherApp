package com.example.weatherapp.favorite.view

import android.view.View
import com.example.weatherapp.model.FavoriteCity

interface FavoriteClickListener {
    fun onDeleteClick(favoriteCity: FavoriteCity)
    fun onNavClick(favoriteCity: FavoriteCity,view: View)
}