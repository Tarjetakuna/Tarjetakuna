package com.github.sdp.tarjetakuna.model

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Represents a pair of coordinates.
 * It is used to represent a location for a user.
 */
data class Coordinates(
    var latitude: Double,
    var longitude: Double
) {

    init {
        latitude = latitude.coerceAtMost(latitudeRange).coerceAtLeast(-latitudeRange)
        longitude = longitude.coerceAtMost(longitudeRange).coerceAtLeast(-longitudeRange)
    }

    /**
     * Calculates the distance in kilometers to another [Coordinates].
     */
    fun distanceKmTo(other: Coordinates): Double {
        // Haversine formula
        val earthRadius = 6371
        val latDistance = Math.toRadians((latitude - other.latitude))
        val lngDistance = Math.toRadians((longitude - other.longitude))
        val a = (sin(latDistance / 2) * sin(latDistance / 2)
                + (cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude))
                * sin(lngDistance / 2) * sin(lngDistance / 2)))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c)
    }

    companion object {
        const val latitudeRange = 90.0
        const val longitudeRange = 180.0
    }

    override fun equals(other: Any?): Boolean {
        val threshold = 0.0001
        if (other is Coordinates) {
            return kotlin.math.abs(this.latitude - other.latitude) <= threshold && kotlin.math.abs(
                this.longitude - other.longitude
            ) <= threshold
        }
        return false
    }
}
