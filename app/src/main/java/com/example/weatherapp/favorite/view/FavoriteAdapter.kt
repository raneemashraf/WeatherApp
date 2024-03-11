package com.example.weatherapp.favorite.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.RowFavBinding
import com.example.weatherapp.model.FavoriteCity

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
            onClickListener.onDeleteClick(favoriteCity)
        }
        holder.binding.favCardView.setOnClickListener(View.OnClickListener {
            onClickListener.onNavClick(favoriteCity, view = holder.binding.favCardView)
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

//: ListAdapter<ProductsItem, ProductViewHolder>(ProductDiffUtil()) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val view: View =
//            layoutInflater.inflate(R.layout.product_item, parent, false)
//        return ProductViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val currentObj = getItem(position)
//        holder.titleTextView.text = currentObj.title
//        Glide.with(holder.img.getContext())
//            .load(currentObj.thumbnail)
//            .into(holder.img)
//
//        holder.favButton.setOnClickListener(View.OnClickListener {
//            listener.onFavClick(currentObj)
//        })
//    }
//
//
//}
//
//class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//    var img: ImageView = view.findViewById(R.id.image_view)
//    var titleTextView: TextView = view.findViewById(R.id.title_text)
//    var favButton: Button = view.findViewById(R.id.favButton)
//    var cardView: ConstraintLayout = view.findViewById(R.id.cardView)
//}
//
//
//class ProductDiffUtil : DiffUtil.ItemCallback<ProductsItem>() {
//    override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
//        return newItem === oldItem
//    }
//
//    override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
//        return oldItem == newItem
//    }
//
//}
