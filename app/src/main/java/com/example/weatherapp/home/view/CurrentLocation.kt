package com.example.weatherapp.home.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder

import android.location.LocationManager
import android.os.Looper
import android.util.Log

import androidx.core.app.ActivityCompat
import com.example.weatherapp.utils.PreferenceManager
import com.google.android.gms.location.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class CurrentLocation(
    var activity: Activity,
    var context: Context,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
) {
    private val REQUEST_CODE = 5
    var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    var myaddress = ""

    companion object {
        @JvmStatic
        val locationStateFlow = MutableStateFlow("0,0")
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), REQUEST_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1000
        mLocationRequest.numUpdates = 1

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        @SuppressLint("SuspiciousIndentation")
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val mLastLocation = locationResult.lastLocation
            longitude = mLastLocation?.longitude as Double
            latitude = mLastLocation?.latitude as Double
            val geocoder = Geocoder(context)
            val theAddress = geocoder.getFromLocation(latitude, longitude, 5)
            if (theAddress?.size!! > 0) {
                myaddress = theAddress[0]?.subAdminArea.toString()
            }
            PreferenceManager.setLatLonGPS(context,longitude.toString(),latitude.toString())
            Log.i("TAG", "onLocationResult: $longitude")
            Log.i("TAG", "onLocationResult: $latitude")

            GlobalScope.launch {
                locationStateFlow.emit(
                    "$longitude,$latitude,$myaddress"
                )
            }

//            if (lastLocation != null) {
//                latitude = lastLocation.latitude
//                longitude = lastLocation.longitude
//
//                sharedPreferences.edit().putString(Constants.GPS_LON, longitude.toString()).apply()
//                sharedPreferences.edit().putString(Constants.GPS_LAT, latitude.toString()).apply()
//
//
//            }else{
//                latitude  =  sharedPreferences.getFloat(Constants.GPS_LAT, 0f).toDouble()
//                longitude = sharedPreferences.getFloat(Constants.GPS_LON, 0f).toDouble()
//            }

        }
    }
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            }
        } else
            requestPermissions()
    }

}