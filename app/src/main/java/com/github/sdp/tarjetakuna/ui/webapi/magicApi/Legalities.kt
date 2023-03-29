package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * Legalities of a MagicCard (need to match the doc in api.magicthegathering.io/v1/)
 */
data class Legalities(
    val format: String,
    val legality: String
)
