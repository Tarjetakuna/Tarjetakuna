package com.github.sdp.tarjetakuna.ui.webapi

import org.junit.Test

/**
 * Override the WebApiViewModel to expose the protected methods for testing
 */
class WebApiViewModelTester : WebApiViewModel() {

    @Test
    fun basicTest() {
        assert(true)
    }

    /*fun getRandomCard() {
        super.getRandomCardWeb()
    }

    fun getSets() {
        super.getSetsWeb()
    }*/

}
