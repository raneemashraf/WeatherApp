package com.example.weatherapp.favorite.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.DataBase.LocalDataSourceImpl
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.repo.RepositoryImpl
import com.example.weatherapp.network.RemoteDataSourceImpl
import com.example.weatherapp.network.api.ApiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment(),FavoriteClickListener {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favouriteAdapter: FavoriteAdapter
    lateinit var favLayoutManager: LinearLayoutManager
    lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addFavFloating.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf("fav" to "fav")
            Navigation.findNavController(view).navigate(R.id.action_favoriteFragment_to_mapFragment,bundle)
        })
        favoriteViewModelFactory = FavoriteViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl(), LocalDataSourceImpl(requireContext())
            ))

        favoriteViewModel = ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)

        favLayoutManager = LinearLayoutManager(activity?.applicationContext as Context)
        favLayoutManager.orientation = RecyclerView.VERTICAL
        favouriteAdapter = FavoriteAdapter(this)
        binding.recyclerViewFav.adapter = favouriteAdapter
        binding.recyclerViewFav.layoutManager = favLayoutManager

        lifecycleScope.launch {
            favoriteViewModel.onlineFavorite.collectLatest {
                when (it) {
                    is ApiState.Loading ->{
                    }
                    is ApiState.Success -> {
                        favouriteAdapter.submitList(it.data)
                    }

                    is ApiState.Failure -> {
                        Toast.makeText(context, "Failed to load products. Please try again later.", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }

    override fun onDeleteClick(favoriteCity: FavoriteCity) {
        favoriteViewModel.deleteFavCity(favoriteCity)
    }

    override fun onNavClick(favoriteCity: FavoriteCity,view: View) {
        val bundle = bundleOf("weather" to favoriteCity)
        Navigation.findNavController(view).navigate(R.id.action_favoriteFragment_to_favoriteWeatherFragment,bundle)
      //  findNavController().navigate(R.id.action_favoriteFragment_to_favoriteWeatherFragment)
    }
}
