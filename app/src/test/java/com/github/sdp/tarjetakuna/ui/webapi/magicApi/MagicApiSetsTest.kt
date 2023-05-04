package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.mockdata.CommonMagicApiSet
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MagicApiSetsTest {
    @Test
    fun validMagicApiSets() {
        val sets = MagicApiSets(
            listOf(
                CommonMagicApiSet.m15
            )
        )

        assertEquals(sets.data[0].id, CommonMagicApiSet.M15.id)
    }
}
