package com.github.sdp.tarjetakuna.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [Coordinates]
 */
class CoordinatesTest {

    private val validLatitude = 3.0f
    private val validLongitude = 42.0f
    private val latitudeOutOfRange = 91.0f
    private val longitudeOutOfRange = 181.0f

    @Test
    fun latitudeMustBeInRange() {
        val coordinatesLatitudeLower = Coordinates(-latitudeOutOfRange, validLongitude)
        val coordinatesLatitudeUpper = Coordinates(latitudeOutOfRange, validLongitude)
        assertEquals(-Coordinates.latitudeRange, coordinatesLatitudeLower.latitude)
        assertEquals(Coordinates.latitudeRange, coordinatesLatitudeUpper.latitude)
    }


    @Test
    fun longitudeMustBeInRange() {
        val coordinatesLongitudeLower = Coordinates(validLatitude, -longitudeOutOfRange)
        val coordinatesLongitudeUpper = Coordinates(validLatitude, longitudeOutOfRange)
        assertEquals(-Coordinates.longitudeRange, coordinatesLongitudeLower.longitude)
        assertEquals(Coordinates.longitudeRange, coordinatesLongitudeUpper.longitude)
    }

    @Test
    fun validCoordinates() {
        val coordinates = Coordinates(validLatitude, validLongitude)
        assertEquals(validLatitude, coordinates.latitude)
        assertEquals(validLongitude, coordinates.longitude)
    }
}
