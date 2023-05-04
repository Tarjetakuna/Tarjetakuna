package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.mockdata.CommonCardFaceApi
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CardFaceApiTest {

    private val validArtist = CommonCardFaceApi.Dragon.artist
    private val validCmc = CommonCardFaceApi.Dragon.cmc
    private val validColorIndicator = CommonCardFaceApi.Dragon.color_indicator
    private val validColors = CommonCardFaceApi.Dragon.colors
    private val validFlavorText = CommonCardFaceApi.Dragon.flavor_text
    private val validIllustrationId = CommonCardFaceApi.Dragon.illustration_id
    private val validImageUris = CommonCardFaceApi.Dragon.image_uris
    private val validLayout = CommonCardFaceApi.Dragon.layout
    private val validLoyalty = CommonCardFaceApi.Dragon.loyalty
    private val validManaCost = CommonCardFaceApi.Dragon.mana_cost
    private val validName = CommonCardFaceApi.Dragon.name
    private val validOracleId = CommonCardFaceApi.Dragon.oracle_id
    private val validOracleText = CommonCardFaceApi.Dragon.oracle_text
    private val validPower = CommonCardFaceApi.Dragon.power
    private val validPrintedName = CommonCardFaceApi.Dragon.printed_name
    private val validPrintedText = CommonCardFaceApi.Dragon.printed_text
    private val validPrintedTypeLine = CommonCardFaceApi.Dragon.printed_type_line
    private val validToughness = CommonCardFaceApi.Dragon.toughness
    private val validTypeLine = CommonCardFaceApi.Dragon.type_line
    private val validWatermark = CommonCardFaceApi.Dragon.watermark

    @Test
    fun validCardFaceApi() {
        val cardFaceApi = CardFaceApi(
            validArtist,
            validCmc,
            validColorIndicator,
            validColors,
            validFlavorText,
            validIllustrationId,
            validImageUris,
            validLayout,
            validLoyalty,
            validManaCost,
            validName,
            validOracleId,
            validOracleText,
            validPower,
            validPrintedName,
            validPrintedText,
            validPrintedTypeLine,
            validToughness,
            validTypeLine,
            validWatermark
        )

        assertEquals(cardFaceApi.artist, validArtist)
        assertEquals(cardFaceApi.cmc, validCmc)
        assertEquals(cardFaceApi.color_indicator, validColorIndicator)
        assertEquals(cardFaceApi.colors, validColors)
        assertEquals(cardFaceApi.flavor_text, validFlavorText)
        assertEquals(cardFaceApi.illustration_id, validIllustrationId)
        assertEquals(cardFaceApi.image_uris, validImageUris)
        assertEquals(cardFaceApi.layout, validLayout)
        assertEquals(cardFaceApi.loyalty, validLoyalty)
        assertEquals(cardFaceApi.mana_cost, validManaCost)
        assertEquals(cardFaceApi.name, validName)
        assertEquals(cardFaceApi.oracle_id, validOracleId)
        assertEquals(cardFaceApi.oracle_text, validOracleText)
        assertEquals(cardFaceApi.power, validPower)
        assertEquals(cardFaceApi.printed_name, validPrintedName)
        assertEquals(cardFaceApi.printed_text, validPrintedText)
        assertEquals(cardFaceApi.printed_type_line, validPrintedTypeLine)
        assertEquals(cardFaceApi.toughness, validToughness)
        assertEquals(cardFaceApi.type_line, validTypeLine)
        assertEquals(cardFaceApi.watermark, validWatermark)
    }
}
