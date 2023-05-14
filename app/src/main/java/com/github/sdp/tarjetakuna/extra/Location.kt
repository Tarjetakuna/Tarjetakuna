package com.github.sdp.tarjetakuna.extra

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.github.sdp.tarjetakuna.model.Coordinates

object Location {


    private var currentLocation: Coordinates = Coordinates(0.0, 0.0)

    private var locationManager: LocationManager? = null

    private var hasAlreadyAsked = false

    /**
     * Get the current location of the user if the permission is granted, if not asked yet, ask for it
     */
    fun captureCurrentLocation(context: AppCompatActivity) {
        // Check if the location permission is granted, and if not, request it
        if (checkPermission(context) && !hasAlreadyAsked) {
            Log.i("Location", "Permission not granted")
            hasAlreadyAsked = true
            requestPermission(context)
            // Do nothing since the user rejected the permission
        } else if (!checkPermission(context)) {
            if (locationManager == null) {
                locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        }
    }

    /**
     * Check if the location permission is granted
     */
    private fun checkPermission(context: AppCompatActivity): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    // What to do when the location changes
    private val locationListener: LocationListener = LocationListener { location ->
        val newLocation = Coordinates(location.latitude, location.longitude)
        if (currentLocation != newLocation) {
            currentLocation = newLocation
            Log.i(
                "Location",
                "Location changed: latitude: ${getCurrentLocation().latitude}, " +
                        "longitude: ${getCurrentLocation().longitude}"
            )
        }
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

    /**
     * Getter for the current location
     */
    fun getCurrentLocation(): Coordinates {
        return currentLocation
    }

    /**
     * Setter for the location manager
     */
    fun setLocationManager(manager: LocationManager) {
        locationManager = manager
    }
}
