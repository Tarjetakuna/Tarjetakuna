// ====================
// MagicDeckTest.kt
// Tarjetakuna, 2023
// ====================

package com.github.bjolidon.bootcamp.model

import org.junit.Test
import org.junit.Assert.*

class MagicDeckTest {

    private val validName = "Best deck ever"
    private val validDescription = "Mono red"
    private val validCards = emptyList<MagicCard>()

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicDeck("", "Mono red", validCards)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicDeck(" ", "Mono red", validCards)
        }
    }

    @Test
    fun validSet() {
        val set = MagicDeck(validName, validDescription, validCards)
        assertEquals(validName, set.name)
        assertEquals(validDescription, set.description)
        assertEquals(validCards, set.cards)
    }
}
