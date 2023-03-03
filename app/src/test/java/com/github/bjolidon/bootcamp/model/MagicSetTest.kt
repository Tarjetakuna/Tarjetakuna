// ====================
// MagicSetTest.kt
// Tarjetakuna, 2023
// ====================

package com.github.bjolidon.bootcamp.model

import org.junit.Test
import org.junit.Assert.*

class MagicSetTest {
    @Test
    fun blankCodeIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet("", "Magic 2015")
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(" ", "Magic 2015")
        }
    }

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet("MT15", "")
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet("MT15", " ")
        }
    }

    @Test
    fun validSet() {
        MagicSet("MT15", "Magic 2015")
    }
}
