package com.github.sdp.tarjetakuna.extra

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.ui.authentication.SignIn

object Location {


    private var currentLocation: Coordinates = Coordinates(0.0, 0.0)

    private var locationManager: LocationManager? = null

    private var hasAlreadyAsked = false

    private var lastPushedToFirebase = System.currentTimeMillis()

    private var firstConnection = true

    /**
     * Get the current location of the user if the permission is granted, if not asked yet, ask for it
     */
    fun captureCurrentLocation(context: Activity) {
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
    private fun checkPermission(context: Activity): Boolean {
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
            pushCoordinateToFirebase(newLocation)
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
    private fun requestPermission(context: Activity) {
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

    /**
     * Push the current location to Firebase
     */
    private fun pushCoordinateToFirebase(currentLocation: Coordinates) {
        val user = UserRTDB(FirebaseDB())
        // push every 5 mins
        if (SignIn.getSignIn().isUserLoggedIn() &&
            (timeElapsed() || firstConnection)
        ) {
            firstConnection = false
            lastPushedToFirebase = System.currentTimeMillis()
            user.pushUserLocation(SignIn.getSignIn().getUserUID()!!, currentLocation)
            Log.i("Location", "Pushed to Firebase")
        } else {
            Log.i("Location", "User not logged in or time not elapsed")
        }
    }

    /**
     * Check if 5 mins have passed since the last push to Firebase
     */
    private fun timeElapsed(): Boolean {
        return System.currentTimeMillis() - lastPushedToFirebase > 300000
    }

    /**
     * Setter for the last pushed to Firebase
     */
    fun setLastPushedToFirebase(time: Long) {
        lastPushedToFirebase = time
    }
}
