package com.github.sdp.tarjetakuna.ui.webapi.magicApi

/**
 * Data class for a MagicCard (need to match the doc in https://api.scryfall.com)
 */
data class MagicApiCard(
    // ================== //
    // Core Card Fields   //
    // ================== //
    val arena_id: Int?,
    val id: String,
    val lang: String,
    val mtgo_id: Int?,
    val mtgo_foil_id: Int?,
    val multiverse_ids: List<Int>,
    val tcgplayer_id: Int?,
    val tcgplayer_etched_id: Int?,
    val cardmarket_id: Int?,
    val oracle_id: String,
    val prints_search_uri: String,
    val rulings_uri: String,
    val scryfall_uri: String,
    val uri: String,

    // ================== //
    // Gameplay Fields    //
    // ================== //
    val all_parts: List<RelatedCardObjectApi>?,
    val card_faces: List<CardFaceApi>?,
    val cmc: Double?,
    val color_identity: List<String>,
    val color_indicator: List<String>?,
    val colors: List<String>?,
    val edhrec_rank: Int?,
    val hand_modifier: String?,
    val keywords: List<String>?,
    val layout: String?,
    val legalities: Map<String, String>,
    val life_modifier: String?,
    val loyalty: String?,
    val mana_cost: String?,
    val name: String,
    val oracle_text: String?,
    val oversized: Boolean,
    val penny_rank: Int?,
    val power: String?,
    val produced_mana: List<String>?,
    val reserved: Boolean,
    val toughness: String?,
    val type_line: String,

    // ================== //
    // Print Fields       //
    // ================== //
    val artist: String?,
    val attraction_lights: List<String>?,
    val booster: Boolean,
    val border_color: String,
    val card_back_id: String,
    val collector_number: String,
    val content_warning: Boolean?,
    val digital: Boolean,
    val finishes: List<String>,
    val flavor_name: String?,
    val flavor_text: String?,
    val frame_effects: List<String>,
    val frame: String,
    val full_art: Boolean,
    val games: List<String>,
    val highres_image: Boolean,
    val illustration_id: String?,
    val image_status: String,
    val image_uris: ImageUrisApi?,
    val prices: Map<String, String>,
    val printed_name: String?,
    val printed_text: String?,
    val printed_type_line: String?,
    val promo: Boolean,
    val promo_types: List<String>,
    val purchase_uris: Map<String, String>,
    val rarity: String,
    val related_uris: Map<String, String>,
    val released_at: String,
    val reprint: Boolean,
    val scryfall_set_uri: String,
    val set_name: String,
    val set_search_uri: String,
    val set_type: String,
    val set_uri: String,
    val set: String,
    val set_id: String,
    val story_spotlight: Boolean,
    val textless: Boolean,
    val variation: Boolean,
    val variation_of: String?,
    val security_stamp: String?,
    val watermark: String?,
)

/**
 * Data class for a [RelatedCardObjectApi] used in [MagicApiCard] (need to match the doc in https://api.scryfall.com)
 */
data class RelatedCardObjectApi(
    val id: String,
    val component: String,
    val name: String,
    val type_line: String,
    val uri: String
)

/**
 * Data class for a [CardFaceApi] used in [MagicApiCard] (need to match the doc in https://api.scryfall.com)
 */
data class CardFaceApi(
    val artist: String?,
    val cmc: Double?,
    val color_indicator: List<String>?,
    val colors: List<String>?,
    val flavor_text: String?,
    val illustration_id: String?,
    val image_uris: ImageUrisApi?,
    val layout: String?,
    val loyalty: String?,
    val mana_cost: String,
    val name: String,
    val oracle_id: String?,
    val oracle_text: String?,
    val power: String?,
    val printed_name: String?,
    val printed_text: String?,
    val printed_type_line: String?,
    val toughness: String?,
    val type_line: String?,
    val watermark: String?
)

/**
 * Data class for a [ImageUrisApi] used in [MagicApiCard] (need to match the doc in https://api.scryfall.com)
 */
data class ImageUrisApi(
    val art_crop: String?,
    val border_crop: String?,
    val large: String?,
    val normal: String?,
    val png: String?,
    val small: String?
)
