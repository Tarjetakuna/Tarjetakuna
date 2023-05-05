package com.github.sdp.tarjetakuna.model

/**
 * Represents a pair of coordinates.
 */
data class Coordinates(
    var latitude: Float,
    var longitude: Float
) {

    init {
        latitude = latitude.coerceAtMost(latitudeRange).coerceAtLeast(-latitudeRange)
        longitude = longitude.coerceAtMost(longitudeRange).coerceAtLeast(-longitudeRange)
    }

    companion object {
        const val latitudeRange = 90.0f
        const val longitudeRange = 180.0f
    }
}
