package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.ui.webapi.magicApi.RelatedCardObjectApi

/**
 * Common [CommonRelatedCardObjectApi] for testing
 */
object CommonRelatedCardObjectApi {

    val elemental = RelatedCardObjectApi(
        id = Elemental.id,
        component = Elemental.component,
        name = Elemental.name,
        type_line = Elemental.type_line,
        uri = Elemental.uri
    )

    object Elemental {
        const val id = "0574973b-a0d1-4e17-9ed7-42a98d25bb8d"
        const val component = "token"
        const val name = "Elemental"
        const val type_line = "Token Creature — Elemental"
        const val uri = "https://api.scryfall.com/cards/0574973b-a0d1-4e17-9ed7-42a98d25bb8d"
    }
}
