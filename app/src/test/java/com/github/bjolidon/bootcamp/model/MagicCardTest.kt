// ====================
// MagicCardTest.kt
// Tarjetakuna, 2023
// ====================

package com.github.bjolidon.bootcamp.model

import org.junit.Test
import org.junit.Assert.*

class MagicCardTest {

    private val validName = "Angel of Mercy"
    private val validText = "Flying"
    private val validLayout = MagicLayout.Normal
    private val validCMC = 7
    private val validManaCost = "{5}{W}{W}"
    private val validSet = MagicSet("MT15", "Magic 2015")
    private val validNumber = 56
    private val validImageUrl = "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    private val validRarity = MagicRarity.Rare
    private val validType = MagicType.Creature
    private val validSubtypes = listOf("Angel")

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard("", validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(" ", validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        }
    }

    @Test
    fun blankTextIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, "", validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, " ", validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        }
    }

    @Test
    fun negativeCMCIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, -1, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        }
    }

    @Test
    fun blankManaCostIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, "", validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, " ", validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        }
    }

    @Test
    fun negativeOrZeroNumberIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, 0, validImageUrl, validRarity, validType, validSubtypes)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, -1, validImageUrl, validRarity, validType, validSubtypes)
        }
    }

    @Test
    fun blankImageUrlIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, "", validRarity, validType, validSubtypes)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, " ", validRarity, validType, validSubtypes)
        }
    }

    @Test
    fun blankSubtypesIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, listOf("", "Angel"))
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, listOf("Angel", " "))
        }
    }

    @Test
    fun validMagicCardDefault() {
        val card = MagicCard()
        assertEquals("Unknown name", card.name)
        assertEquals("Unknown text", card.text)
        assertEquals(MagicLayout.Normal, card.layout)
        assertEquals(0, card.convertedManaCost)
        assertEquals("{0}", card.manaCost)
        assertEquals(MagicSet("Unknown code", "Unknown name"), card.set)
        assertEquals(1, card.number)
        assertEquals("https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card", card.imageUrl)
        assertEquals(MagicRarity.Common, card.rarity)
        assertEquals(MagicType.Creature, card.type)
        assertEquals(listOf<String>(), card.subtypes)
    }

    @Test
    fun validMagicCard() {
        val card = MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl, validRarity, validType, validSubtypes)
        assertEquals(validName, card.name)
        assertEquals(validText, card.text)
        assertEquals(validLayout, card.layout)
        assertEquals(validCMC, card.convertedManaCost)
        assertEquals(validManaCost, card.manaCost)
        assertEquals(validSet, card.set)
        assertEquals(validNumber, card.number)
        assertEquals(validImageUrl, card.imageUrl)
        assertEquals(validRarity, card.rarity)
        assertEquals(validType, card.type)
        assertEquals(validSubtypes, card.subtypes)
    }
}
