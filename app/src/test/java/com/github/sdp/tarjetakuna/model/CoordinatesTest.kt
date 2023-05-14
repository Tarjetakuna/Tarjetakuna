package com.github.sdp.tarjetakuna.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [Coordinates]
 */
class CoordinatesTest {

    private val validLatitude = 3.0
    private val validLongitude = 42.0
    private val latitudeOutOfRange = 91.0
    private val longitudeOutOfRange = 181.0

    @Test
    fun latitudeMustBeInRange() {
        val coordinatesLatitudeLower = Coordinates(-latitudeOutOfRange, validLongitude)
        val coordinatesLatitudeUpper = Coordinates(latitudeOutOfRange, validLongitude)
        assertEquals(-Coordinates.latitudeRange, coordinatesLatitudeLower.latitude, 0.001)
        assertEquals(Coordinates.latitudeRange, coordinatesLatitudeUpper.latitude, 0.001)
    }


    @Test
    fun longitudeMustBeInRange() {
        val coordinatesLongitudeLower = Coordinates(validLatitude, -longitudeOutOfRange)
        val coordinatesLongitudeUpper = Coordinates(validLatitude, longitudeOutOfRange)
        assertEquals(-Coordinates.longitudeRange, coordinatesLongitudeLower.longitude, 0.001)
        assertEquals(Coordinates.longitudeRange, coordinatesLongitudeUpper.longitude, 0.001)
    }

    @Test
    fun validCoordinates() {
        val coordinates = Coordinates(validLatitude, validLongitude)
        assertEquals(validLatitude, coordinates.latitude, 0.001)
        assertEquals(validLongitude, coordinates.longitude, 0.001)
    }

    @Test
    fun EqualityWorks() {
        val coordinates1 = Coordinates(validLatitude, validLongitude)
        val coordinates2 = Coordinates(validLatitude, validLongitude)
        assertEquals(true, coordinates1 == coordinates2)
    }

    @Test
    fun EqualityWorks2() {
        val coordinates1 = Coordinates(validLatitude, validLongitude)
        val coordinates2 = Coordinates(validLatitude + 10, validLongitude + 0.0001)
        assertEquals(false, coordinates1 == coordinates2)
    }

    @Test
    fun notACoordEqualsFalse() {
        val coordinates1 = Coordinates(validLatitude, validLongitude)
        val coordinates2 = null
        assertEquals(false, coordinates1 == coordinates2)
    }
}
