package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * Set of MagicCards (need to match the doc in https://api.scryfall.com)
 */
data class MagicApiSet(
    val id: String,
    val code: String,
    val mtgo_code: String?,
    val tcgplayer_id: Int?,
    val name: String,
    val set_type: String,
    val released_at: String?,
    val block_code: String?,
    val block: String?,
    val parent_set_code: String?,
    val card_count: Int,
    val printed_size: Int?,
    val digital: Boolean,
    val foil_only: Boolean,
    val nonfoil_only: Boolean,
    val scryfall_uri: String?,
    val uri: String?,
    val icon_svg_uri: String?,
    val search_uri: String?,
)
