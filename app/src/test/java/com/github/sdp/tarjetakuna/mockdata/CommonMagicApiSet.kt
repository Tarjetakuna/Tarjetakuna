package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicApiSet

/**
 * Common [MagicApiSet] for testing
 */
object CommonMagicApiSet {

    /**
     * Set of Magic 2015
     */
    val m15 = MagicApiSet(
        id = M15.id,
        code = M15.code,
        mtgo_code = M15.mtgo_code,
        tcgplayer_id = M15.tcgplayer_id,
        name = M15.name,
        set_type = M15.set_type,
        released_at = M15.released_at,
        block_code = M15.block_code,
        block = M15.block,
        parent_set_code = M15.parent_set_code,
        card_count = M15.card_count,
        printed_size = M15.printed_size,
        digital = M15.digital,
        foil_only = M15.foil_only,
        nonfoil_only = M15.nonfoil_only,
        scryfall_uri = M15.scryfall_uri,
        uri = M15.uri,
        icon_svg_uri = M15.icon_svg_uri,
        search_uri = M15.search_uri
    )

    /**
     * Data of Magic 2015
     */
    object M15 {

        const val id = "6ce49890-3b37-42a5-8932-dbeef1d7b62c"
        const val code = "m15"
        const val mtgo_code = "m15"
        const val tcgplayer_id = 1293
        const val name = "Magic 2015"
        const val set_type = "core"
        const val released_at = "2014-07-18"
        const val block_code = "lea"
        const val block = "Core Set"
        val parent_set_code = null
        const val card_count = 284
        const val printed_size = 269
        const val digital = false
        const val foil_only = false
        const val nonfoil_only = false
        const val scryfall_uri = "https://scryfall.com/sets/m15"
        const val uri = "https://api.scryfall.com/sets/6ce49890-3b37-42a5-8932-dbeef1d7b62c"
        const val icon_svg_uri = "https://svgs.scryfall.io/sets/m15.svg?1682308800"
        const val search_uri = "https://api.scryfall.com/cards/search?include_extras=true&include_variations=true&order=set&q=e%3Am15&unique=prints"
    }
}
