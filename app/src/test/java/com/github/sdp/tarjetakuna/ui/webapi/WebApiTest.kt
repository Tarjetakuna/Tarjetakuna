package com.github.sdp.tarjetakuna.ui.webapi

import org.junit.Test

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
}
