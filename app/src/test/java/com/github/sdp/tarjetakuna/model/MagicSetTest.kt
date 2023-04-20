// ====================
// MagicSetTest.kt
// Tarjetakuna, 2023
// ====================

package com.github.sdp.tarjetakuna.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate

class MagicSetTest {

    private val validCode = "MT15"
    private val validName = "Magic 2015"
    private val validType = "Core"
    private val validBlock = "Core"
    private val validReleaseDate = LocalDate.parse("2014-07-18")

    @Test
    fun blankCodeIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet("", validName, validType, validBlock, validReleaseDate)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(" ", validName, validType, validBlock, validReleaseDate)
        }
    }

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, "", validType, validBlock, validReleaseDate)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, " ", validType, validBlock, validReleaseDate)
        }
    }

    @Test
    fun blankTypeIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, "", validBlock, validReleaseDate)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, " ", validBlock, validReleaseDate)
        }
    }

    @Test
    fun blankBlockIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, validType, "", validReleaseDate)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, validType, " ", validReleaseDate)
        }
    }

    @Test
    fun validSetWithDefaultConstructor() {
        val set = MagicSet()
        assertEquals("Unknown code", set.code)
        assertEquals("Unknown name", set.name)
        assertEquals("Unknown type", set.type)
        assertEquals("Unknown block", set.block)
        assertEquals(LocalDate.parse("1970-01-01"), set.releaseDate)
    }

    @Test
    fun validSet() {
        val set = MagicSet(validCode, validName, validType, validBlock, validReleaseDate)
        assertEquals(validCode, set.code)
        assertEquals(validName, set.name)
        assertEquals(validType, set.type)
        assertEquals(validBlock, set.block)
        assertEquals(validReleaseDate, set.releaseDate)
    }
}
