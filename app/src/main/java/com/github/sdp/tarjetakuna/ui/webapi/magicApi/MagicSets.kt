package com.github.sdp.tarjetakuna.ui.webapi.magicApi


data class MagicSets(
    val sets: List<MagicSet>
) {
    override fun toString(): String {
        return sets.joinToString(separator = "\n\n")
    }
}
