// ====================
// MagicCardTest.kt
// Tarjetakuna, 2023
// ====================

package com.github.sdp.tarjetakuna.model

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

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard("", validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(" ", validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl)
        }
    }

    @Test
    fun blankTextIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, "", validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, " ", validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl)
        }
    }

    @Test
    fun negativeCMCIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, -1, validManaCost, validSet, validNumber, validImageUrl)
        }
    }

    @Test
    fun blankManaCostIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, "", validSet, validNumber, validImageUrl)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, " ", validSet, validNumber, validImageUrl)
        }
    }

    @Test
    fun negativeOrZeroNumberIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, 0, validImageUrl)
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, -1, validImageUrl)
        }
    }

    @Test
    fun blankImageUrlIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, "")
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, " ")
        }
    }

    @Test
    fun validMagicCard() {
        val card = MagicCard(validName, validText, validLayout, validCMC, validManaCost, validSet, validNumber, validImageUrl)
        assertEquals(validName, card.name)
        assertEquals(validText, card.text)
        assertEquals(validLayout, card.layout)
        assertEquals(validCMC, card.convertedManaCost)
        assertEquals(validManaCost, card.manaCost)
        assertEquals(validSet, card.set)
        assertEquals(validNumber, card.number)
        assertEquals(validImageUrl, card.imageUrl) }
}
