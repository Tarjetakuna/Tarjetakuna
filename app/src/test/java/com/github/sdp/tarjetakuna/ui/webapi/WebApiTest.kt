package com.github.sdp.tarjetakuna.ui.webapi

import org.junit.Test

/**
 * Test for WebApi - mostly done in the androidTest as need UI
 */
class WebApiTest {

    private val magicUrlTest = "https://api.magicthegathering.io/v1/"

    @Test
    fun test_url() {
        val api = WebApi()
        assert(api.magicUrl == magicUrlTest)
    }


    @Test
    fun test_getMagicApi() {
        val api = WebApi.getMagicApi()
        assert(api.getSets().isExecuted.not())
        assert(api.getCards().isExecuted.not())
    }

    @Test
    fun test_getCards(){
        val fCards = WebApi.getCards()
        fCards.whenComplete { cards, throwable ->
            assert(cards != null)
            assert(throwable == null)
            assert(cards.cards.isNotEmpty())
            assert(cards.cards.size == 5)
            assert(cards.cards[0].name.isNotEmpty())
            assert(cards.cards[0].name == "Ancestor's Chosen")
            assert(cards.cards[0].imageUrl.isNotEmpty())
        }.get()
    }
}
