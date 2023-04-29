package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.CardFaceApi

/**
 * Common [CommonCardFaceApi] to use in tests
 */
object CommonCardFaceApi {

    val dragon = CardFaceApi(
        artist = Dragon.artist,
        cmc = Dragon.cmc,
        color_indicator = Dragon.color_indicator,
        colors = Dragon.colors,
        flavor_text = Dragon.flavor_text,
        illustration_id = Dragon.illustration_id,
        image_uris = Dragon.image_uris,
        layout = Dragon.layout,
        loyalty = Dragon.loyalty,
        mana_cost = Dragon.mana_cost,
        name = Dragon.name,
        oracle_id = Dragon.oracle_id,
        oracle_text = Dragon.oracle_text,
        power = Dragon.power,
        printed_name = Dragon.printed_name,
        printed_text = Dragon.printed_text,
        printed_type_line = Dragon.printed_type_line,
        toughness = Dragon.toughness,
        type_line = Dragon.type_line,
        watermark = Dragon.watermark
    )

    object Dragon {
        const val artist = "Ryan Yee"
        const val cmc = 3.0
        val color_indicator = listOf("Red")
        val colors = listOf("Red")
        const val flavor_text = "The essence of a dragon, concentrated in form."
        const val illustration_id = "d0a1b2c3-4e56-78f9-0d1a-2b3c4d5e6f7g"
        val image_uris = CommonImageUrisApi.maskOfImmolation
        const val layout = "normal"
        const val loyalty = "Loyalty: 0"
        const val mana_cost = "{3}{R}"
        const val name = "Dragon Egg"
        const val oracle_id = "d0a1b2c3-4e56-78f9-0d1a-2b3c4d5e6f7g"
        const val oracle_text = "Defender\nWhen Dragon Egg dies, create a 2/2 red Dragon creature token with flying and \"{R}: This creature gets +1/+0 until end of turn.\""
        const val power = "3"
        const val printed_name = "Dragon Egg"
        const val printed_text = "Defender\nWhen Dragon Egg dies, create a 2/2 red Dragon creature token with flying and \"{R}: This creature gets +1/+0 until end of turn.\""
        const val printed_type_line = "Creature — Dragon Egg"
        const val toughness = "3"
        const val type_line = "Creature — Dragon Egg"
        const val watermark = "set"
    }
}
