package com.example.weatherapp.map.view

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.DataBase.LocalDataSourceImpl
import com.example.weatherapp.R
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.map.viewmodel.MapViewModel
import com.example.weatherapp.map.viewmodel.MapViewModelFactory
import com.example.weatherapp.model.FavoriteCity
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.model.repo.RepositoryImpl
import com.example.weatherapp.network.RemoteDataSourceImpl
import com.example.weatherapp.utils.PreferenceManager.setAppLocationByMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions




class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var googleMap: GoogleMap
    private lateinit var saveButton: Button
    private var address: String = ""
    lateinit var destination: String
    lateinit var viewModel: MapViewModel
    private lateinit var myView: View
    private lateinit var geocoder: Geocoder
    lateinit var favoriteCity: FavoriteCity
    private lateinit var viewModelFactory: MapViewModelFactory
    lateinit var favoriteViewModelFactory :FavoriteViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        destination = arguments?.get("fav") as String

        viewModelFactory = MapViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl(), LocalDataSourceImpl(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)

        favoriteViewModelFactory = FavoriteViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl(), LocalDataSourceImpl(requireContext())
            ))

        favoriteViewModel = ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)


        geocoder = Geocoder(requireContext())
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        saveButton = view.findViewById(R.id.btn_saveLocation)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMapClickListener {
            val marker = MarkerOptions()
            marker.position(it)
            googleMap.clear()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 5f))
            googleMap.addMarker(marker)
            val lat = it.latitude
            val lon = it.longitude
            address = getCityName(lon, lat)
            marker.title(address)
            Log.i("MapFragment", "onViewCreated:$address")
            saveButton.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.apply {
                    setIcon(R.drawable.add_location)
                    setTitle(getString(R.string.add_location))
                    setMessage("Are you sure you want to add ${address} to favorite?")
                    setPositiveButton(getString(R.string.yes)) { _: DialogInterface?, _: Int ->
                        if (destination == "fav") {
                            favoriteCity = FavoriteCity(
                                cityName = address,
                                longitude = lon,
                                latitude = lat,
                                latLog = lat.toString().plus(lon.toString())
                            )
                            viewModel.insertCityToFav(favoriteCity)
                            favoriteViewModel.getAllFavCity()
                            view?.let { it1 ->
                                Navigation.findNavController(it1)
                                    .navigate(R.id.action_mapFragment_to_favoriteFragment)
                            }
                        } else {
                            setAppLocationByMap(requireContext(), lon.toString(), lat.toString())
                            view?.let { it1 ->
                                Navigation.findNavController(it1)
                                    .navigate(R.id.action_mapFragment_to_homeFragment2)
                            }
                        }
                    }
                    setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    }.create().show()
                }
            }

//                .setPositiveButton("Save",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        Toast.makeText(requireContext(),"Saved Successfully",Toast.LENGTH_SHORT).show()
//                        if(type  == "fav")
//                            viewModel.addPlaceToFav(place)
//                        else
//                            setAppLocationByMap(requireContext(),place.longitude.toString(),place.latitude.toString())
//
//                    })
//                .setNegativeButton("Cancel",


        }

    }

    fun getCityName(longitude: Double, latitude: Double): String {
        try {
            val theAddress = geocoder.getFromLocation(latitude, longitude, 5)
            if (theAddress!!.isNotEmpty()) {
                return theAddress[0]?.adminArea ?: ""
            } else {
                return ""
            }
        } catch (e: Exception) {
            Log.e("MapFragmentError", "Error getting city name: ${e.message}")
            return ""
        }
    }
}
