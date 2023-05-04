package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * List of [MagicApiCard] (need to match the doc in https://api.scryfall.com)
 * This data class is mandatory to receive the data from the api (a list of [MagicApiCard] is not enough)
 */
data class MagicApiCards(
    val total_cards: Int,
    val has_more: Boolean,
    val next_page: String?,
    val data: List<MagicApiCard>
)
