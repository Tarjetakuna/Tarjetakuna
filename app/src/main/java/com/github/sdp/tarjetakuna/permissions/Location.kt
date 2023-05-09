package com.github.sdp.tarjetakuna.permissions

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

object Location {

    /**
     * Check if the location permission is granted, and if not, request it
     */
    private fun locationPermissionGranted(context: AppCompatActivity): Boolean {
        val requestcode = 1
        return if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
            false
        } else {
            true
        }
    }

    /**
     * Get the current location of the user
     */
    fun getCurrentLocation(context: AppCompatActivity) {
        if (locationPermissionGranted(context)) {
//            val locationManager = getSystemService(context, LocationManager::class.java)
//            val hasGps = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            val hasNetwork = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }


    }
}
