package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * List of MagicCard (need to match the doc in api.magicthegathering.io/v1/)
 */
class MagicApiCards(
    val cards: List<MagicCard>
) {
    override fun toString(): String {
        return cards.joinToString(separator = "\n\n")
    }
}
