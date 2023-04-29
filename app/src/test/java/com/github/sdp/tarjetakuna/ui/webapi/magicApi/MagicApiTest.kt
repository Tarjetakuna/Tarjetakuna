package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Test for MagicApi - mostly done in the androidTest as need UI
 */
class MagicApiTest {

    private lateinit var api: MagicApi

    @Before
    fun setUp() {
        api = createMagicApi()
    }

    /**
     * Test that the call is not executed but that the call is created
     */
    @Test
    fun test_GetCards() {
        val call = api.getRandomCard()

        assert(call.isExecuted.not())
    }

    /**
     * Test that the call is not executed but that the call is created
     */
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
