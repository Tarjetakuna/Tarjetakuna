@file:Suppress("DEPRECATION")

package com.github.sdp.tarjetakuna.utils

import androidx.activity.result.ActivityResult

/**
 * This class is responsible to manage the compatibility between the different versions of Android
 */
object Compatibility {

    /**
     * Returns the value associated with the given key or null if no mapping of the desired type exists for the given key.
     */
    fun <T> getDataActivityResult(activityResult: ActivityResult, key: String, clazz: Class<T>): T? {
        return if (android.os.Build.VERSION.SDK_INT >= 33) {
            activityResult.data?.extras?.getParcelable(key, clazz)
        } else {
            clazz.cast(activityResult.data?.extras?.get(key))
        }
    }
}
