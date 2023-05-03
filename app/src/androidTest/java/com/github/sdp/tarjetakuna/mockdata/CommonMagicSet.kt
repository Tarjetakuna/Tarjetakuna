package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.model.MagicSet
import java.time.LocalDate

/**
 * This is a common class that can be used to get [MagicSet] objects
 */
object CommonMagicSet {

    /**
     * This is a valid [MagicSet] Magic 2015
     */
    val magic2015Set: MagicSet = MagicSet(
        "m15",
        "Magic 2015",
        "core",
        "Core Set",
        LocalDate.parse("2014-07-18"),
        "https://svgs.scryfall.io/sets/m15.svg?1682913600",
        284,
        "",
        "https://api.scryfall.com/cards/search?include_extras=true&include_variations=true&order=set&q=e%3Am15&unique=prints"
    )

    /**
     * This is a valid [MagicSet] Promo Magic 2015
     */
    val promoMagic2015Set: MagicSet = MagicSet(
        "pm15",
        "Magic 2015 Promos",
        "promo",
        "",
        LocalDate.parse("2014-07-17"),
        "https://svgs.scryfall.io/sets/m15.svg?1682913600",
        13,
        "m15",
        "https://api.scryfall.com/cards/search?include_extras=true&include_variations=true&order=set&q=e%3Apm15&unique=prints"
    )

}
