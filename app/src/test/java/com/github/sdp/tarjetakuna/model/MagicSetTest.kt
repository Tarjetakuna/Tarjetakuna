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
    private val validReleaseDate = LocalDate.parse("2014-07-18")
    private val validIconUri = "https://svgs.scryfall.io/sets/m15.svg?1682913600"
    private val validCardCount = 13
    private val validParentSetCode = "m15"
    private val validCardsInSetUri = "https://api.scryfall.com/cards/search?include_extras=true&include_variations=true&order=set&q=e%3Apm15&unique=prints"

    @Test
    fun blankCodeIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet("", validName, validType, validReleaseDate, validIconUri, validCardCount, validParentSetCode, validCardsInSetUri)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(" ", validName, validType, validReleaseDate, validIconUri, validCardCount, validParentSetCode, validCardsInSetUri)
        }
    }

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, "", validType, validReleaseDate, validIconUri, validCardCount, validParentSetCode, validCardsInSetUri)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, " ", validType, validReleaseDate, validIconUri, validCardCount, validParentSetCode, validCardsInSetUri)
        }
    }

    @Test
    fun blankTypeIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, "", validReleaseDate, validIconUri, validCardCount, validParentSetCode, validCardsInSetUri)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, " ", validReleaseDate, validIconUri, validCardCount, validParentSetCode, validCardsInSetUri)
        }
    }

    @Test
    fun blankIconUriIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, validType, validReleaseDate, "", validCardCount, validParentSetCode, validCardsInSetUri)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, validType, validReleaseDate, " ", validCardCount, validParentSetCode, validCardsInSetUri)
        }
    }

    @Test
    fun negativeValidCardCountIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicSet(validCode, validName, validType, validReleaseDate, validIconUri, -1, validParentSetCode, validCardsInSetUri)
        }
    }

    @Test
    fun validSet() {
        val set = MagicSet(validCode, validName, validType, validReleaseDate, validIconUri, validCardCount, validParentSetCode, validCardsInSetUri)
        assertEquals(validCode, set.code)
        assertEquals(validName, set.name)
        assertEquals(validType, set.type)
        assertEquals(validReleaseDate, set.releaseDate)
        assertEquals(validIconUri, set.iconUri)
        assertEquals(validCardCount, set.cardCount)
        assertEquals(validParentSetCode, set.parentSetCode)
        assertEquals(validCardsInSetUri, set.cardsInSetUri)
    }
}
