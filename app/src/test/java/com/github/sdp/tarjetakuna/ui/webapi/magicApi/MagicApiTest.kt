package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MagicApiTest {

    private lateinit var api: MagicApi

    @Before
    fun setUp() {
        api = createMagicApi()
    }

    @Test
    fun test_GetCards() {
        val call = api.getCards()

        assert(call.isExecuted.not())
    }

    @Test
    fun test_GetSets() {
        val call = api.getSets()

        assert(call.isExecuted.not())
    }

    private fun createMagicApi(): MagicApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MagicApi::class.java)
    }
}
