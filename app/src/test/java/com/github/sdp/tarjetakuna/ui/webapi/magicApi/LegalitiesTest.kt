package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.ResourceHelper
import com.google.gson.Gson
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.After
import org.junit.Before
import org.junit.Test


class LegalitiesTest {
    lateinit var card: MagicCard
    val gson = Gson()

    @Before
    fun setUp() {
        val cardJson =
            ResourceHelper.ResourceHelper.loadString("magic_webapi_cardById_386616_response.json")
        card = gson.fromJson(cardJson, MagicApiCard::class.java).card
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_fields() {

        val format_list = listOf(
            "commander",
            "duel",
            "legacy",
            "modern",
            "penny",
            "pioneer",
            "vintage"
        )
        val legality_list = listOf("banned", "legal", "not_legal", "restricted")

        card.legalities.forEach {
            assertThat("legality not empty", it.legality.isNotEmpty())
            assertThat("format not empty", it.format.isNotEmpty())
            assertThat("format is in the list", format_list, hasItem(it.format.lowercase()))
            assertThat("legality is in the list", legality_list, hasItem(it.legality.lowercase()))
        }
    }
}
