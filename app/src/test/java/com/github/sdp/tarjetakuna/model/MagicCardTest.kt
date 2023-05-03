package com.github.sdp.tarjetakuna.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate

class MagicCardTest {

    private val validName = "Angel of Mercy"
    private val validText = "Flying"
    private val validLayout = MagicLayout.NORMAL
    private val validCMC = 7
    private val validManaCost = "{5}{W}{W}"
    private val validSet = MagicSet("MT15", "Magic 2015", "Core", LocalDate.parse("2014-07-18"), "iconUriBlabla", 13, "m15", "cardsInSetUriBlabla")
    private val validNumber = 56
    private val validImageUrl =
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    private val validRarity = MagicRarity.RARE
    private val validType = MagicCardType.CREATURE
    private val validSubtypes = listOf("Angel")
    private val validArtist = "Artist"
    private val validPower = "3"
    private val validToughness = "*"

    @Test
    fun blankNameIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                "",
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                " ",
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun blankTextIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                "",
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                " ",
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun negativeCMCIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                -1,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun blankManaCostIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                "",
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                " ",
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun negativeOrZeroNumberIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                0,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                -1,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun blankImageUrlIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                "",
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                " ",
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun blankSubtypesIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                listOf("", "Angel"),
                validPower,
                validToughness,
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                listOf("Angel", " "),
                validPower,
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun blankArtistIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                ""
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                validToughness,
                " "
            )
        }
    }

    @Test
    fun blankPowerIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                "",
                validToughness,
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                " ",
                validToughness,
                validArtist
            )
        }
    }

    @Test
    fun blankToughnessIsInvalid() {
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                "",
                validArtist
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            MagicCard(
                validName,
                validText,
                validLayout,
                validCMC,
                validManaCost,
                validSet,
                validNumber,
                validImageUrl,
                validRarity,
                validType,
                validSubtypes,
                validPower,
                " ",
                validArtist
            )
        }
    }

    @Test
    fun validMagicCard() {
        val card = MagicCard(
            validName,
            validText,
            validLayout,
            validCMC,
            validManaCost,
            validSet,
            validNumber,
            validImageUrl,
            validRarity,
            validType,
            validSubtypes,
            validPower,
            validToughness,
            validArtist
        )
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
        assertEquals(validPower, card.power)
        assertEquals(validToughness, card.toughness)
        assertEquals(validArtist, card.artist)
    }
}
