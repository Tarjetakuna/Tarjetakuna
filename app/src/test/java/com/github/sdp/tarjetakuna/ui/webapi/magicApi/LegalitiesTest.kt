package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.Test


class LegalitiesTest {
    @Test
    fun test_fields() {

        val formatList = listOf(
            "commander",
            "duel",
            "legacy",
            "modern",
            "penny",
            "pioneer",
            "vintage"
        )
        val legalityList = listOf("banned", "legal", "not_legal", "restricted")

        val card = TestHelperWebApi.getCardByIdResponse()
        card.legalities.forEach {
            assertThat("legality not empty", it.legality.isNotEmpty())
            assertThat("format not empty", it.format.isNotEmpty())
            assertThat("format is in the list", formatList, hasItem(it.format.lowercase()))
            assertThat("legality is in the list", legalityList, hasItem(it.legality.lowercase()))
        }
    }
}
