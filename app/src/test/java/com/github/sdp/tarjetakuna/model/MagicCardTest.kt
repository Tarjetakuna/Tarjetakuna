package com.github.sdp.tarjetakuna.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MagicCardTest {

    private val validName = "Angel of Mercy"
    private val validText = "Flying"
    private val validLayout = MagicLayout.Normal
    private val validCMC = 7
    private val validManaCost = "{5}{W}{W}"
    private val validSet = MagicSet("MT15", "Magic 2015")
    private val validNumber = 56
    private val validImageUrl =
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    private val validRarity = MagicRarity.Rare
    private val validType = MagicCardType.Creature
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
    fun validMagicCardDefault() {
        val card = MagicCard()
        assertEquals("Unknown name", card.name)
        assertEquals("Unknown text", card.text)
        assertEquals(MagicLayout.Normal, card.layout)
        assertEquals(0, card.convertedManaCost)
        assertEquals("{0}", card.manaCost)
        assertEquals(MagicSet(), card.set)
        assertEquals(1, card.number)
        assertEquals(
            "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
            card.imageUrl
        )
        assertEquals(MagicRarity.Common, card.rarity)
        assertEquals(MagicCardType.Creature, card.type)
        assertEquals(listOf<String>(), card.subtypes)
        assertEquals("0", card.power)
        assertEquals("0", card.toughness)
        assertEquals("Unknown artist", card.artist)
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
