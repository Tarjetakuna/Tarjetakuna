package com.github.sdp.tarjetakuna.ui.webapi.magicApi

class MagicCards(
    val cards: List<MagicCard>
) {
    override fun toString(): String {
        return cards.joinToString(separator = "\n\n")
    }
}
