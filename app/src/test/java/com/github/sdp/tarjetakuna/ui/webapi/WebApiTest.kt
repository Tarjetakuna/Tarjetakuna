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
    fun test_setUrl() {
        val api = WebApi()
        val url = "https://api.magicthegathering.io/v2/"
        api.magicUrl = url
        assert(api.magicUrl == url)
    }

    @Test
    fun test_setUrl2() {
        val url = "https://api.magicthegathering.io/v2/"
        WebApi.magicUrl = url
        assert(WebApi.magicUrl == url)
    }
}
