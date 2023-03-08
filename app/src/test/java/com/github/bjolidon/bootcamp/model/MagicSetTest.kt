// ====================
// MagicSetTest.kt
// Tarjetakuna, 2023
// ====================

package com.github.bjolidon.bootcamp.model

import org.junit.Test
import org.junit.Assert.*

class MagicSetTest {

    private val validCode = "MT15"
    private val validName = "Magic 2015"

    @Test
    fun blankCodeIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet("", validName)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(" ", validName)
        }
    }

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, "")
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, " ")
        }
    }

    @Test
    fun validSet() {
        val set = MagicSet(validCode, validName)
        assertEquals(validCode, set.code)
        assertEquals(validName, set.name)
    }
}
