package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class MagicSetTest {
    lateinit var set: MagicSet

    @Before
    fun setUp() {
        set = TestHelperWebApi.getSetByIdResponse()
    }

    @Test
    fun test_fieldNotNull() {
        assertThat("code not null", set.code.isNotEmpty())
        assertThat("name not null", set.name.isNotEmpty())
        assertThat("type not null", set.type.isNotEmpty())
        if (set.border != null) {
            assertThat("border not null", set.border!!.isNotEmpty())
        }
        if (set.mkm_id != null) {
            assertThat("mkm_id not null", set.mkm_id!!.isNotEmpty())
        }
        if (set.mkm_name != null) {
            assertThat("mkm_name not null", set.mkm_name!!.isNotEmpty())
        }
        if (set.gathererCode != null) {
            assertThat("gathererCode not null", set.gathererCode!!.isNotEmpty())
        }
        if (set.magicCardsInfoCode != null) {
            assertThat("magicCardsInfoCode not null", set.magicCardsInfoCode!!.isNotEmpty())
        }
        assertThat("releaseDate not null", set.releaseDate.isNotEmpty())
        assertThat("block not null", set.block.isNotEmpty())
        assertThat("onlineOnly not null", set.onlineOnly, isA(Boolean::class.java))
        if (set.mkm_idExpansion != null) {
            assertThat("mkm_idExpansion not null", set.mkm_idExpansion!!.isNotEmpty())
        }
        if (set.mkm_nameExpansion != null) {
            assertThat("mkm_nameExpansion not null", set.mkm_nameExpansion!!.isNotEmpty())
        }
    }

    @Test
    fun test_toString() {
        assertThat("toString not null", set.toString().isNotEmpty())
    }
}