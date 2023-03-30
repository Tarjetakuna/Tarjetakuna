package com.github.sdp.tarjetakuna.ui.webapi

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.`is`
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
    fun test_getCards() {
        test_getCards_witRetry(5)
    }

    private fun test_getCards_witRetry(retry: Int) {
        val fCards = WebApi.getCards()
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsByName_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.cards, `is`(notNullValue()))
                assertThat("cards size bigger than 5", cards?.cards?.size!!, greaterThan(5))

                assertThat(
                    "first card imageUrl not empty",
                    cards.cards[0].imageUrl,
                    `is`(not(String()))
                )
                assertThat(
                    "first card name is Ancestor's Chosen",
                    cards.cards[0].name,
                    `is`("Ancestor's Chosen")
                )
            }
        }.get()
    }

    @Test
    fun test_getCardsByName() {
        test_getCardsByName_withRetry(5)
    }

    private fun test_getCardsByName_withRetry(retry: Int) {
        val fCards = WebApi.getCardsByName("Ancestor's Chosen")
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsByName_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.cards, `is`(notNullValue()))
                assertThat("cards size bigger than 1", cards?.cards?.size!!, greaterThan(1))

                assertThat(
                    "first card imageUrl not empty",
                    cards.cards[0].imageUrl,
                    `is`(not(String()))
                )
                assertThat(
                    "first card name is Ancestor's Chosen",
                    cards.cards[0].name,
                    `is`("Ancestor's Chosen")
                )
            }
        }.get()
    }

    @Test
    fun test_getCardById() {
        test_getCardById(5)
    }

    private fun test_getCardById(retry: Int) {
        val fCard = WebApi.getCardById("386616")
        fCard.whenComplete { card, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardById(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", card, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))

                assertThat("imageUrl not empty", card.imageUrl, `is`(not(String())))
                assertThat("name is", card.name, `is`("Narset, Enlightened Master"))
                assertThat("id is", card.multiverseid, `is`("386616"))
            }
        }.get()
    }

    @Test
    fun test_getCardsBySet() {
        test_getCardsBySet_withRetry(5)
    }

    private fun test_getCardsBySet_withRetry(retry: Int) {
        val fCards = WebApi.getCardsBySet("KTK")
        fCards.whenComplete { cards, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("cards is not null", cards, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("cards.cards not null", cards?.cards, `is`(notNullValue()))
                assertThat("cards size bigger than 1", cards?.cards?.size!!, greaterThan(1))

                for (card in cards.cards) {
                    assertThat("card set is KTK", card.set, `is`("KTK"))
                    assertThat("card imageUrl not empty", card.imageUrl, `is`(not(String())))
                }
            }
        }.get()
    }

    @Test
    fun test_getSets() {
        test_getSets_withRetry(5)
    }

    private fun test_getSets_withRetry(retry: Int) {
        val fSets = WebApi.getSets()
        fSets.whenComplete { sets, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("sets is not null", sets, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))
                assertThat("sets.sets not null", sets?.sets, `is`(notNullValue()))
                assertThat("sets size bigger than 1", sets?.sets?.size!!, greaterThan(1))

                for (set in sets.sets) {
                    assertThat("set code not empty", set.code, `is`(not(String())))
                    assertThat("set name not empty", set.name, `is`(not(String())))
                }
            }
        }.get()
    }

    @Test
    fun test_getSetByCode() {
        test_getSetByCode_withRetry(5)
    }

    private fun test_getSetByCode_withRetry(retry: Int) {
        val fSet = WebApi.getSetByCode("KTK")
        fSet.whenComplete { set, throwable ->
            if (throwable != null && retry > 0) {
                test_getCardsBySet_withRetry(retry - 1)
            } else {
                assertThat("retry is > 0", retry, `is`(greaterThan(0)))
                assertThat("set is not null", set, `is`(notNullValue()))
                assertThat("future complete without error", throwable, `is`(nullValue()))

                assertThat("set code is KTK", set.code, `is`("KTK"))
                assertThat("set name is Khans of Tarkir", set.name, `is`("Khans of Tarkir"))
            }
        }.get()
    }
}
