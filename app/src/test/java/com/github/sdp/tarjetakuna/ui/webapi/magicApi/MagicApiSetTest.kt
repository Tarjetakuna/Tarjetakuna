package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.mockdata.CommonMagicApiSet
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MagicApiSetTest {

    lateinit var set: MagicApiSet

    @Test
    fun validMagicApiSet() {
        set = CommonMagicApiSet.m15
        assertEquals(set.id, CommonMagicApiSet.M15.id)
        assertEquals(set.code, CommonMagicApiSet.M15.code)
        assertEquals(set.mtgo_code, CommonMagicApiSet.M15.mtgo_code)
        assertEquals(set.tcgplayer_id, CommonMagicApiSet.M15.tcgplayer_id)
        assertEquals(set.name, CommonMagicApiSet.M15.name)
        assertEquals(set.set_type, CommonMagicApiSet.M15.set_type)
        assertEquals(set.released_at, CommonMagicApiSet.M15.released_at)
        assertEquals(set.block_code, CommonMagicApiSet.M15.block_code)
        assertEquals(set.block, CommonMagicApiSet.M15.block)
        assertEquals(set.parent_set_code, CommonMagicApiSet.M15.parent_set_code)
        assertEquals(set.card_count, CommonMagicApiSet.M15.card_count)
        assertEquals(set.printed_size, CommonMagicApiSet.M15.printed_size)
        assertEquals(set.digital, CommonMagicApiSet.M15.digital)
        assertEquals(set.foil_only, CommonMagicApiSet.M15.foil_only)
        assertEquals(set.nonfoil_only, CommonMagicApiSet.M15.nonfoil_only)
        assertEquals(set.scryfall_uri, CommonMagicApiSet.M15.scryfall_uri)
        assertEquals(set.uri, CommonMagicApiSet.M15.uri)
        assertEquals(set.icon_svg_uri, CommonMagicApiSet.M15.icon_svg_uri)
        assertEquals(set.search_uri, CommonMagicApiSet.M15.search_uri)
    }
}
