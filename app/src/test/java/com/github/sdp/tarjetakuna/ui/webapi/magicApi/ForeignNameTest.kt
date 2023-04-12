package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.ResourceHelper
import com.google.gson.Gson
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ForeignNameTest {

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
        card.foreignNames.forEach {
            assertThat("name not empty", it.name.isNotEmpty())
            assertThat("text not empty", it.text.isNotEmpty())
            assertThat("type not empty", it.type.isNotEmpty())
            assertThat("flavor value is null", it.flavor.toString(), equalTo("null"))
            assertThat("imageUrl not empty", it.imageUrl.isNotEmpty())
            assertThat("language not empty", it.language.isNotEmpty())
            assertThat("multiverseid not empty", it.multiverseid.toString().isNotEmpty())
        }
    }
}
