package com.github.sdp.tarjetakuna.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Utils {
    /**
     * Check if the device is connected to the internet
     */
    companion object {
        /**
         * Check if the device is connected to the internet
         */
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

        /**
         * return a string with the description and the value if the value is not null or empty
         */
        fun printIfNotNullOrEmpty(ob: String?, description: String): String {
            return if (ob != null && ob.isNotEmpty()) {
                description + ob
            } else {
                ""
            }
        }

        /**
         * return a string with the description and the value if the value is not null or empty
         */
        fun printIfNotNullOrEmpty(ob: Boolean?, description: String): String {
            return if (ob != null) {
                description + ob
            } else {
                ""
            }
        }

        /**
         * Synchronously wait and retry for a given time in milliseconds and a given number of times,
         * UNTIL the predicate becomes true.
         *
         * It will wait for the given time, then retry the given number of times, waiting in each loop
         */
        fun waitUntilTrue(timeToWait: Long, numberOfRetries: Int, predicate: () -> Boolean) {
            var retries = 0
            while (!predicate() && retries < numberOfRetries) {
                Thread.sleep(timeToWait)
                retries++
            }
            if (retries == numberOfRetries) {
                throw Exception("Waited for $timeToWait ms and retried $numberOfRetries times, but the predicate was never true")
            }
        }

        /**
         * Synchronously wait and retry for a given time in milliseconds and a given number of times,
         * WHILE the predicate becomes true.
         *
         * It will wait for the given time, then retry the given number of times, waiting in each loop
         */
        fun waitWhileTrue(timeToWait: Long, numberOfRetries: Int, predicate: () -> Boolean) {
            var retries = 0
            while (predicate() && retries < numberOfRetries) {
                Thread.sleep(timeToWait)
                retries++
            }
            if (retries == numberOfRetries) {
                throw Exception("Waited for $timeToWait ms and retried $numberOfRetries times, but the predicate was never true")
            }
        }

    }
}
