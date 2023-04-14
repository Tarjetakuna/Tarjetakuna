package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MagicApiCardTest {
    @Test
    fun test_ConstructorWithCard() {
        val card = TestHelperWebApi.getCardByIdResponse()
        val testCard = MagicApiCard(card)

        assertThat("card are equal", testCard.card, equalTo(card))
    }

    @Test
    fun test_callToString() {
        val card = TestHelperWebApi.getCardByIdResponse()
        val testCard = MagicApiCard(card)

        assertThat("toString not empty", testCard.toString().isNotEmpty())
    }
}
