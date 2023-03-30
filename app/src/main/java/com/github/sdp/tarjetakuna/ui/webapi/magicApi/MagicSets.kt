package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * List of MagicSets (need to match the doc in api.magicthegathering.io/v1/)
 */
data class MagicSets(
    val sets: List<MagicSet>
) {
    override fun toString(): String {
        return sets.joinToString(separator = "\n\n")
    }
}
