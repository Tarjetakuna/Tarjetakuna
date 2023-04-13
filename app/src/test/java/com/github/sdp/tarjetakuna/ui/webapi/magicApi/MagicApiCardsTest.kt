package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MagicApiCardsTest {
    @Test
    fun test_ConstructorWithCards() {
        val cards = TestHelperWebApi.getCardsResponse()
        val testCards = MagicApiCards(cards)

        assertThat("cards not empty", testCards.cards.isNotEmpty())
        assertThat("cards are equal", testCards.cards, equalTo(cards))
    }

    @Test
    fun test_callToString() {
        val cards = TestHelperWebApi.getCardsResponse()
        val testCards = MagicApiCards(cards)

        assertThat("toString not empty", testCards.toString().isNotEmpty())
    }
}
