package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * 1 single card from the api (need to match the doc in api.magicthegathering.io/v1/)
 */
data class MagicApiCard(
    val card: MagicCard
) {
    override fun toString(): String {
        return card.toString()
    }
}
