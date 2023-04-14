package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.TestHelperWebApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MagicApiSetTest {
    @Test
    fun test_ConstructorWithSet() {
        val set = TestHelperWebApi.getSetByIdResponse()
        val testSet = MagicApiSet(set)

        assertThat("sets are equal", testSet.set, equalTo(set))
    }

    @Test
    fun test_callToString() {
        val set = TestHelperWebApi.getSetByIdResponse()
        val testSet = MagicApiSet(set)

        assertThat("toString not empty", testSet.toString().isNotEmpty())
    }

    @Test
    fun test_stringEqual() {
        val set = TestHelperWebApi.getSetByIdResponse()
        val testSet = MagicApiSet(set)

        assertThat("toString equals to set", testSet.toString(), equalTo(set.toString()))
    }
}
