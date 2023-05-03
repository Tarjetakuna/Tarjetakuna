package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import junit.framework.TestCase.assertEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class MagicCardTest {
    lateinit var card: MagicCard

    @Before
    fun setUp() {
        card = TestHelperWebApi.getCardByIdResponse()
    }

    @Test
    fun test_fieldsNotEmpty() {
        assertThat("name not empty", card.name.isNotEmpty())
        assertThat("manaCost not empty", card.manaCost.isNotEmpty())
        assertThat("cmc not empty", card.cmc != 0)
        assertThat("colors not empty", card.colors.isNotEmpty())
        assertThat("colorIdentity not empty", card.colorIdentity.isNotEmpty())
        assertThat("type not empty", card.type.isNotEmpty())
        assertThat("types not empty", card.types.isNotEmpty())
        assertThat("subtypes not empty", card.subtypes.isNotEmpty())
        assertThat("rarity not empty", card.rarity.isNotEmpty())
        assertThat("set not empty", card.set.isNotEmpty())
        assertThat("setName not empty", card.setName.isNotEmpty())
        assertThat("text not empty", card.text.isNotEmpty())
        assertThat("artist not empty", card.artist.isNotEmpty())
        assertThat("number not empty", card.number.isNotEmpty())
        assertThat("power not empty", card.power.isNotEmpty())
        assertThat("toughness not empty", card.toughness.isNotEmpty())
        assertThat("layout not empty", card.layout.isNotEmpty())
        assertThat("multiverseid not empty", card.multiverseid.isNotEmpty())
        assertThat("imageUrl not empty", card.imageUrl.isNotEmpty())
        // variations can be null
        if (card.variations != null) {
            assertThat("variations not empty", card.variations!!.isNotEmpty())
        }
        assertThat("foreignNames not empty", card.foreignNames.isNotEmpty())
        assertThat("printings not empty", card.printings.isNotEmpty())
        assertThat("originalText not empty", card.originalText.isNotEmpty())
        assertThat("originalType not empty", card.originalType.isNotEmpty())
        assertThat("legalities not empty", card.legalities.isNotEmpty())
        assertThat("id not empty", card.id.isNotEmpty())
    }

    @Test
    fun test_toString() {
        assertThat("toString not null", card.toString().isNotEmpty())
    }

    @Test
    fun test_validMagicCard() {
        val validForeignName = ForeignName(
            "name", "text", "type", null, "imageUrl", "language", 1
        )
        val validLegalities = Legalities("Format", "Legality")
        val validMagicCard = MagicCard(
            "name", "manaCost", 1, listOf("colors"), listOf("colorIdentity"), "type", listOf("types"),
            listOf("subtypes"), "rarity", "set", "setName", "text", "artist", "number", "power",
            "toughness", "layout", "multiverseid", "imageUrl", listOf("variations"), listOf(validForeignName),
            listOf("printings"), "originalText", "originalType", listOf(validLegalities), "id"
        )

        assertEquals("name", validMagicCard.name)
        assertEquals("manaCost", validMagicCard.manaCost)
        assertEquals(1, validMagicCard.cmc)
        assertEquals(listOf("colors"), validMagicCard.colors)
        assertEquals(listOf("colorIdentity"), validMagicCard.colorIdentity)
        assertEquals("type", validMagicCard.type)
        assertEquals(listOf("types"), validMagicCard.types)
        assertEquals(listOf("subtypes"), validMagicCard.subtypes)
        assertEquals("rarity", validMagicCard.rarity)
        assertEquals("set", validMagicCard.set)
        assertEquals("setName", validMagicCard.setName)
        assertEquals("text", validMagicCard.text)
        assertEquals("artist", validMagicCard.artist)
        assertEquals("number", validMagicCard.number)
        assertEquals("power", validMagicCard.power)
        assertEquals("toughness", validMagicCard.toughness)
        assertEquals("layout", validMagicCard.layout)
        assertEquals("multiverseid", validMagicCard.multiverseid)
        assertEquals("imageUrl", validMagicCard.imageUrl)
        assertEquals(listOf("variations"), validMagicCard.variations)
        assertEquals(listOf(validForeignName), validMagicCard.foreignNames)
        assertEquals(listOf("printings"), validMagicCard.printings)
        assertEquals("originalText", validMagicCard.originalText)
        assertEquals("originalType", validMagicCard.originalType)
        assertEquals(listOf(validLegalities), validMagicCard.legalities)
        assertEquals("id", validMagicCard.id)
    }
}
