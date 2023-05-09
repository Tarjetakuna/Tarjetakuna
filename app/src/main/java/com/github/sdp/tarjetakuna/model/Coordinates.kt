package com.github.sdp.tarjetakuna.model

import java.lang.Math.abs

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

    companion object {
        const val latitudeRange = 90.0
        const val longitudeRange = 180.0
    }

    fun isSameCoordAs(other: Coordinates): Boolean {
        val threshold = 0.0001
        return abs(this.latitude - other.latitude) <= threshold && abs(this.longitude - other.longitude) <= threshold
    }
}
