package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class MagicApiSetsTest {
    @Test
    fun test_ConstructorWithSets() {
        val sets = TestHelperWebApi.getSetsResponse()
        val testSets = MagicApiSets(sets)

        MatcherAssert.assertThat("sets are equal", testSets.sets, equalTo(sets))
    }

    @Test
    fun test_callToString() {
        val sets = TestHelperWebApi.getSetsResponse()
        val testSets = MagicApiSets(sets)

        MatcherAssert.assertThat("toString not empty", testSets.toString().isNotEmpty())
    }
}
