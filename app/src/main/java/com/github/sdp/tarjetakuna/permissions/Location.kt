package com.github.sdp.tarjetakuna.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

object Location {

    /**
     * Check if the location permission is granted, and if not, request it
     */
    fun askForLocationPermission(context: AppCompatActivity): Boolean {
        val requestcode = 1
        return if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(context)
            false
        } else {
            true
        }
    }

    /**
     * Get the current location of the user if the permission is granted
     */
    fun getCurrentLocation(context: AppCompatActivity) {
        val requestcode = 1
        // Check if the location permission is granted, and if not, request it
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Do nothing since the user rejected the permission
        } else {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        }

    }

    // What to do when the location changes
    private val locationListener: LocationListener = LocationListener { location ->
        println(location.latitude)
        println(location.longitude)
    }

    /**
     * Request the location permission
     */
    private fun requestPermission(context: AppCompatActivity) {
        val requestcode = 1
        ActivityCompat.requestPermissions(
            context,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            requestcode
        )
    }
}
