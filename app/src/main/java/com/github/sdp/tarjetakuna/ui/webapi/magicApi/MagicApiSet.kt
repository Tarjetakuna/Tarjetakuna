package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * 1 single MagicSet to receive from api (need to match the doc in api.magicthegathering.io/v1/)
 */
data class MagicApiSet(
    val set: MagicSet
) {
    override fun toString(): String {
        return set.toString()
    }
}
