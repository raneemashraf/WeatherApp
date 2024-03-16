package com.example.weatherapp.favorite.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.RowFavBinding
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.utils.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar

class FavoriteAdapter(val onClickListener: FavoriteClickListener)
    : ListAdapter<FavoriteCity, FavoriteAdapter.ViewHolder>(DiffUtils){

    class ViewHolder(val binding: RowFavBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteCity = getItem(position)
        val context = holder.binding.root.context
        holder.binding.txtFavCountryName.text = favoriteCity.cityName
       holder.binding.imageFavDelete.setOnClickListener {
           val alertDialog = AlertDialog.Builder(context)

           alertDialog.apply {
               setTitle("Delete")
               setMessage("Are you sure you want to delete ${favoriteCity.cityName} ?")
               setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                   onClickListener.onDeleteClick(favoriteCity)
                   Snackbar.make(
                       holder.binding.root,
                       "The alert deleted successfully",
                       Snackbar.LENGTH_LONG
                   ).show()
               }
               setNegativeButton("Cancel") { _, _ ->
               }
           }.create().show()
        }

        holder.binding.favCardView.setOnClickListener(View.OnClickListener {
            if (isNetworkAvailable(context)) {
                onClickListener.onNavClick(favoriteCity, view = holder.binding.favCardView)
            } else {
                Snackbar.make(
                    holder.binding.root,
                    "You're offline, Check Internet Connection",
                    Snackbar.ANIMATION_MODE_FADE
                ).show()
            }
        })
    }

    object DiffUtils : DiffUtil.ItemCallback<FavoriteCity>() {
        override fun areItemsTheSame(oldItem: FavoriteCity, newItem: FavoriteCity): Boolean {
            return oldItem.latLog == newItem.latLog
        }

        override fun areContentsTheSame(oldItem: FavoriteCity, newItem: FavoriteCity): Boolean {
            return oldItem == newItem
        }

    }
}
