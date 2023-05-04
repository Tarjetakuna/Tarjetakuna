package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * List of [MagicApiSet] (need to match the doc in https://api.scryfall.com)
 * This data class is mandatory to receive the data from the api (a list of [MagicApiSet] is not enough)
 */
data class MagicApiSets(
    val data: List<MagicApiSet>
)
