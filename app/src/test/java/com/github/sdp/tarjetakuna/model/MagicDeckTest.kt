package com.github.sdp.tarjetakuna.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Tests for [MagicDeck]
 */
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
