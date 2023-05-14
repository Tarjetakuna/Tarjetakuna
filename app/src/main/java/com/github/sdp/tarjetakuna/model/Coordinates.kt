package com.github.sdp.tarjetakuna.model

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
