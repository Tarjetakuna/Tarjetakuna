package com.github.sdp.tarjetakuna.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Utils {
    /**
     * Check if the device is connected to the internet
     */
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                // for other device that are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                // for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        }

        // return a string with the description and the value if the value is not null or empty
        fun printIfNotNullOrEmpty(ob: String?, description: String) : String {
            return if (ob != null && ob.isNotEmpty()) {
                description + ob
            } else {
                ""
            }
        }

        // return a string with the description and the value if the value is not null or empty
        fun printIfNotNullOrEmpty(ob: Boolean?, description: String) : String {
            return if (ob != null) {
                description + ob
            } else {
                ""
            }
        }
    }
}
