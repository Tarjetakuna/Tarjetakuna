package com.github.sdp.tarjetakuna.ui.webapi

/**
 * Override the WebApiViewModel to expose the protected methods for testing
 */
class WebApiViewModelTester : WebApiViewModel() {
    
    fun getCards() {
        super.getCardsWeb()
    }

    fun getSets() {
        super.getSetsWeb()
    }

}
